package com.jamesellerbee.taskfire.tasktrackerapi.app.dal.properties

import java.io.File
import java.util.Properties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ApplicationProperties(private val path: String) {
    private val properties = Properties().also {
        it.load(File(path).reader())
    }

    operator fun <T> get(key: String): Any? {
        return properties[key]
    }

    fun get(key: String, defaultValue: Any): Any {
        return properties[key] ?: defaultValue
    }

    operator fun set(key: String, value: Any) {
        properties[key] = value

        CoroutineScope(SupervisorJob()).launch {
            properties.store(File(path).outputStream(), null)
        }
    }
}