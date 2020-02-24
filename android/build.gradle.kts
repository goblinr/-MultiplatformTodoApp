plugins {
    // kotlin("multiplatform")
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {

    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        applicationId = "com.a65apps.multiplatform.sample"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    composeOptions {
        kotlinCompilerVersion = "1.3.70-dev-withExperimentalGoogleExtensions-20200424"
        kotlinCompilerExtensionVersion = "0.1.0-dev13"
    }

    androidExtensions {
        isExperimental = true
    }
}

dependencies {
    val reaktiveVersion: String by rootProject.extra
    val daggerVersion: String by rootProject.extra

    val retrofitVersion = "2.4.0"
    val okhttpVersion = "3.14.1"
    val composeVersion = "0.1.0-dev13"

    implementation(project(":interaction"))

    // Android
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // Compose
    implementation("androidx.ui:ui-tooling:$composeVersion")
    implementation("androidx.ui:ui-layout:$composeVersion")
    implementation("androidx.ui:ui-material:$composeVersion")
    implementation("androidx.compose:compose-runtime:$composeVersion")

    // Dagger
    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    // Network
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

    testImplementation("junit:junit:4.12")
    testImplementation("com.badoo.reaktive:reaktive-testing:$reaktiveVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
