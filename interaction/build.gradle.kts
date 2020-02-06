import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

val reaktiveVersion: String by rootProject.extra

kotlin {

    // select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64
    val reaktiveIos = if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            "ios64"
        else
            "iossim"

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "Interaction"
                export(project(":domain"))
                export("com.badoo.reaktive:reaktive-$reaktiveIos:$reaktiveVersion")
                freeCompilerArgs = freeCompilerArgs + "-Xobjc-generics"
            }
        }
    }

    macosX64("macos") {
        binaries {
            framework {
                baseName = "Interaction"
                export(project(":domain"))
            }
        }
    }

    jvm("android")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
                implementation("com.badoo.reaktive:reaktive:$reaktiveVersion")
                implementation("com.badoo.reaktive:reaktive-annotations:$reaktiveVersion")
                api(project(":domain"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test-common")
                implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
                implementation("com.badoo.reaktive:reaktive-testing:$reaktiveVersion")
                implementation("com.badoo.reaktive:utils:$reaktiveVersion")
            }
        }

        val androidTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlin:kotlin-test-junit")
                implementation("com.badoo.reaktive:reaktive-testing-jvm:$reaktiveVersion")
            }
        }

        val iosMain by getting {
            dependencies {
                api("com.badoo.reaktive:reaktive-$reaktiveIos:$reaktiveVersion")
            }
        }
    }
}

val makeXcodeFrameworkBuildDir by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")
    targetDir.mkdirs()
    val gradlew = File(targetDir, "gradlew")
    gradlew.createNewFile()
    gradlew.writeText("#!/bin/bash\n" +
            "export 'JAVA_HOME=${System.getProperty("java.home")}'\n" +
            "cd '${rootProject.rootDir}'\n" +
            "./gradlew \$@\n")
    gradlew.setExecutable(true)
}

val packForXcode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    // / selecting the right configuration for the iOS
    // / framework depending on the environment
    // / variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    // / generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\n" +
                "export 'JAVA_HOME=${System.getProperty("java.home")}'\n" +
                "cd '${rootProject.rootDir}'\n" +
                "./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)
