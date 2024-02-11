package com.jamesellerbee.taskfireandroid.dal.taskfire

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class TaskFireApi(baseUrl: String) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getUnsafeOkHttpClient())
        .build()

    val taskFireService = retrofit.create<TaskFireService>()

    private val _authenticated = MutableStateFlow(false)
    val authenticated = _authenticated.asStateFlow()

    private var _authToken: String? = null
    val authToken get() = _authToken!!

    private var _accountId: String? = null
    val accountId get() = _accountId!!

    fun setAuthToken(token: String) {
        _authToken = "Bearer $token"
        _authenticated.value = true
    }

    fun setAccountId(accountId: String) {
        _accountId = accountId
    }

    // TODO: either figure out how to only accept the server certificate or replace self-signed certificate.
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        val trustManagers: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return emptyArray()
                }
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustManagers, SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManagers[0] as X509TrustManager)
            .hostnameVerifier(HostnameVerifier { hostname, session -> true })
            .build()
    }
}