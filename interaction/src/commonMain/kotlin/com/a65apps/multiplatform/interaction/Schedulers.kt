package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.scheduler.Scheduler

interface Schedulers {

    val computation: Scheduler

    val io: Scheduler

    val main: Scheduler
}
