// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion by extra("1.3.72")
    val reaktiveVersion by extra("1.1.13")
    val daggerVersion by extra("2.26")

    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/badoo/maven")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha02")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.4.0"
}

allprojects {

    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/badoo/maven")
    }
}

subprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        failFast = true // fail build on any finding
        buildUponDefaultConfig = true // preconfigure defaults
        input = files(
            "src/main/kotlin",
            "src/commonMain/kotlin",
            "src/androidMain/kotlin",
            "src/iosMain/kotlin",
            "src/macosMain/kotlin"
        )

        reports {
            html.enabled = true // observe findings in your browser with structure and code snippets
            xml.enabled = true // checkstyle like format mainly for integrations like Jenkins
            txt.enabled = true // similar to the console output, contains issue signature to manually edit baseline files
        }
    }

    tasks {
        withType<io.gitlab.arturbosch.detekt.Detekt> {
            // Target version of the generated JVM bytecode. It is used for type resolution.
            this.jvmTarget = "1.8"
        }
    }
}

tasks.withType<Delete> {
    delete(rootProject.buildDir)
}
