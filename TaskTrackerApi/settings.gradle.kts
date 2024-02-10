pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.22"
        kotlin("plugin.serialization") version "1.9.22"
        id("com.github.johnrengelman.shadow") version "8.1.1"
    }

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

rootProject.name = "TaskTrackerApi"
include(":app")

