package com.a65apps.multiplatform.sample.di

import android.app.Application
import android.content.Context

class TodoApplication : Application() {

    val appComponent by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }
}

val Context.component: ApplicationComponent
    get() = (applicationContext as TodoApplication).appComponent
