package com.jamesellerbee.taskfire.tasktrackerapi.app.interfaces

import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.entites.Account

interface AccountRepository {
    fun addAccount(newAccount: Account)

    fun getAccounts(): List<Account>

    fun getAccount(accountId: String): Account?
}