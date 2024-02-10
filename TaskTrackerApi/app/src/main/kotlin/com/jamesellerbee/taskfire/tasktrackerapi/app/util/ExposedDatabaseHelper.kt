package com.jamesellerbee.taskfire.tasktrackerapi.app.util

import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.properties.ApplicationProperties
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.DatabaseType
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.account.ExposedAccountRepository
import java.io.File
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

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
                applicationProperties.get("databaseDriver", "") as String
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

                    transaction {
                        addLogger(StdOutSqlLogger)

                        SchemaUtils.create(ExposedAccountRepository.Accounts)
                    }
                }
            }
        }

        return database!!
    }
}