package com.jamesellerbee.tasktrackerapi.app.bl.account

import com.jamesellerbee.tasktrackerapi.app.dal.entites.Account

class AccountManager {
    private val _accounts = mutableMapOf<String, Account>()
    val accounts get() = _accounts.toMap()

    fun addAccount(newAccount: Account) {
        _accounts[newAccount.id] = newAccount
    }
}