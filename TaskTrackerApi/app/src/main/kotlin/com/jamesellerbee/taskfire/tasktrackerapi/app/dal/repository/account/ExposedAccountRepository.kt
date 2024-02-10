package com.jamesellerbee.taskfire.tasktrackerapi.app.dal.repository.account

import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.Account
import com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces.AccountRepository
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ExposedDatabaseHelper
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class ExposedAccountRepository(serviceLocator: ServiceLocator) : AccountRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val database = ExposedDatabaseHelper.init(serviceLocator)

    override fun addAccount(newAccount: Account) {
        transaction(database) {
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

        transaction(database) {
            AccountEntity.all().forEachIndexed { _, accountEntity ->
                accounts.add(accountEntity.toAccount())
            }
        }

        return accounts.toList()
    }

    override fun getAccount(accountId: String): Account? {
        var account: Account? = null

        transaction {
            AccountEntity.find { Accounts.accountId eq accountId }.firstOrNull()?.let { accountEntity ->
                account = accountEntity.toAccount()
            }
        }

        return account
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

        fun toAccount(): Account {
            return Account(
                name = name,
                password = password,
                id = accountId
            )
        }
    }
}