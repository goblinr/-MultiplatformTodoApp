package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.scheduler.Scheduler
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler

class IosSchedulers : Schedulers {

    override val computation: Scheduler
        get() = computationScheduler
    override val io: Scheduler
        get() = ioScheduler
    override val main: Scheduler
        get() = mainScheduler
}
