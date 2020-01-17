package com.a65apps.multiplatform.sample.core

import com.a65apps.multiplatform.interaction.Schedulers
import com.badoo.reaktive.scheduler.Scheduler
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import javax.inject.Inject

class AndroidSchedulers @Inject constructor() : Schedulers {
    override val computation: Scheduler
        get() = computationScheduler
    override val io: Scheduler
        get() = ioScheduler
    override val main: Scheduler
        get() = mainScheduler
}
