package com.jamesellerbee.taskfire.tasktrackerapi.app.dal.stmp

import com.jamesellerbee.taskfire.tasktrackerapi.app.dal.properties.ApplicationProperties
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ResolutionStrategy
import com.jamesellerbee.taskfire.tasktrackerapi.app.util.ServiceLocator
import org.apache.commons.mail.SimpleEmail

class GoogleSmtpEmailSender(serviceLocator: ServiceLocator) {
    private val applicationProperties by serviceLocator.resolveLazy<ApplicationProperties>(ResolutionStrategy.ByType(type = ApplicationProperties::class))

    fun sendVerificationEmail(recipientEmail: String, verificationLink: String) {
        val email = getSimpleEmail()
        email.addTo(recipientEmail)
        email.subject = "Welcome to Taskfire!"
        email.setMsg("Click the following link to get verified and start tracking tasks.\n\n$verificationLink")
        email.send()
    }

    private fun getSimpleEmail(): SimpleEmail {
        val account = applicationProperties["emailAccount"] as String
        val password = applicationProperties["emailPassword"] as String

        return SimpleEmail().also {
            it.hostName = "smtp.googlemail.com"
            it.setSmtpPort(465)
            it.setAuthentication(
                account,
                password
            )

            it.setSSLOnConnect(true)
            it.setStartTLSRequired(true)
            it.setFrom(account.plus("@gmail.com"))
        }
    }
}