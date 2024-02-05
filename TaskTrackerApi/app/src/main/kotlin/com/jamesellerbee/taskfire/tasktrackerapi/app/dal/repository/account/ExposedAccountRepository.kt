package com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.account

import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.Account
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.properties.ApplicationProperties
import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.DatabaseType
import com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces.AccountRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ResolutionStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class ExposedAccountRepository(serviceLocator: ServiceLocator) : AccountRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val applicationProperties by serviceLocator.resolveLazy<ApplicationProperties>(
        ResolutionStrategy.ByType(
            type = ApplicationProperties::class
        )
    )

    init {
        val databaseType = DatabaseType.valueOf(
            applicationProperties.get("databaseDriver", "") as String
        )

        when (databaseType) {
            DatabaseType.SQLITE -> {
                val sqliteDbPath = applicationProperties.get(
                    "sqliteDbPath",
                    ""
                ) as String

                Database.connect(
                    "jdbc:sqlite:$sqliteDbPath", "org.sqlite.JDBC"
                )
            }
        }

        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Accounts)
        }
    }

    override fun addAccount(newAccount: Account) {
        transaction {
            val existingAccounts =
                AccountEntity.find { (Accounts.name eq newAccount.name) or (Accounts.accountId eq newAccount.id) }
            if (existingAccounts.empty()) {
                AccountEntity.new {
                    name = newAccount.name
                    password = newAccount.password
                    accountId = newAccount.id
                }
            } else {
                logger.warn("There already exists an account with this name or id")
            }
        }
    }

    override fun getAccounts(): List<Account> {
        val accounts = mutableListOf<Account>()

        // TODO fix this
//        transaction {
//            accounts.addAll(AccountEntity.all().toList())
//
//        }

        return accounts
    }

    override fun getAccount(accountId: String): Account? {
        TODO("Not yet implemented")
    }

    object Accounts : IntIdTable() {
        val name = varchar("name", 50)
        val password = varchar("password", 256)
        val accountId = varchar("accountId", 50)
    }

    class AccountEntity(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<AccountEntity>(Accounts)

        var name by Accounts.name
        var password by Accounts.password
        var accountId by Accounts.accountId
    }
}