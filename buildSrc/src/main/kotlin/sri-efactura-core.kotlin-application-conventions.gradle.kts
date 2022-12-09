@file:Suppress("PropertyName")

import org.gradle.kotlin.dsl.application

val kotlin_jvm_target_version: String by project

plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = kotlin_jvm_target_version
    }
}
