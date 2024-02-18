package com.jamesellerbee.taskfire.tasktrackerapi.app.util

import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.properties.ApplicationProperties
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.DatabaseType
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.account.ExposedAccountRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.account.ExposedAdminRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.task.ExposedTaskRepository
import java.io.File
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

object ExposedDatabaseHelper {
    private var database: Database? = null
    fun init(serviceLocator: ServiceLocator): Database {
        if (database == null) {
            val applicationProperties = serviceLocator.resolve<ApplicationProperties>(
                ResolutionStrategy.ByType(
                    type = ApplicationProperties::class
                )
            )!!

            val databaseType = DatabaseType.valueOf(
                applicationProperties.get("databaseDriver", "")
            )

            when (databaseType) {
                DatabaseType.SQLITE -> {
                    val sqliteDbPath = applicationProperties.get(
                        "sqliteDbPath",
                        ""
                    ) as String

                    // Create sqlite db file if it doesn't already exist
                    val file = File(sqliteDbPath)
                    file.parentFile.mkdirs()
                    file.createNewFile()

                    database = Database.connect(
                        "jdbc:sqlite:$sqliteDbPath", "org.sqlite.JDBC"
                    )

                    transaction(database) {
                        addLogger(StdOutSqlLogger)

                        SchemaUtils.create(ExposedAccountRepository.Accounts)
                        SchemaUtils.create(ExposedTaskRepository.Tasks)
                        SchemaUtils.create(ExposedAdminRepository.Admins)

                        AccountsSqliteMigrationHelper.migrate(this)
                    }
                }
            }
        }

        return database!!
    }
}

object AccountsSqliteMigrationHelper {
    fun migrate(transaction: Transaction) {
        val logger = LoggerFactory.getLogger(this::class.java)
        logger.info("Migrating Accounts table")

        val existingColumns = transaction.exec("PRAGMA table_info(Accounts);") {
            val columns = mutableListOf<String>()
            while (it.next()) {
                columns.add(it.getString(2))
            }

            columns.toList()
        } ?: emptyList()

        if (existingColumns.none { it == "created" }) {
            transaction.exec("ALTER TABLE Accounts ADD COLUMN created BIGINT DEFAULT 0;")
        } else {
            logger.debug("Accounts table already has column created. Nothing to do.")
        }
    }
}