package com.a65apps.multiplatform.interaction.mock

import com.a65apps.multiplatform.interaction.Schedulers
import com.badoo.reaktive.scheduler.Scheduler
import com.badoo.reaktive.scheduler.trampolineScheduler

class TestSchedulers : Schedulers {

    override val computation: Scheduler = trampolineScheduler

    override val io: Scheduler = trampolineScheduler

    override val main: Scheduler = trampolineScheduler
}
