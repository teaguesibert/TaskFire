package com.jamesellerbee.taskfireandroid.bl.usecase

import com.jamesellerbee.taskfireandroid.dal.settings.AppSettings
import com.jamesellerbee.taskfireandroid.dal.taskfire.TaskFireApi
import com.jamesellerbee.taskfireandroid.util.ResolutionStrategy
import com.jamesellerbee.taskfireandroid.util.ServiceLocator

class SignOutUseCase(serviceLocator: ServiceLocator) {
    private val taskFireApi by serviceLocator.resolveLazy<TaskFireApi>(
        ResolutionStrategy.ByType(
            type = TaskFireApi::class
        )
    )

    private val appSettings by serviceLocator.resolveLazy<AppSettings>(
        ResolutionStrategy.ByType(type = AppSettings::class)
    )

    operator fun invoke() {
        appSettings.set(AppSettings.rememberCredentials, false.toString())
        appSettings.remove(AppSettings.savedUsername)
        appSettings.remove(AppSettings.savedPassword)
        taskFireApi.logout()
    }
}