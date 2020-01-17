package com.a65apps.multiplatform.sample.di

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class TodoApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.create()
}
