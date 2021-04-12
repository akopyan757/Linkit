package com.akopyan757.linkit_domain.usecase

import io.reactivex.Scheduler

abstract class SchedulerProvider {
    abstract val ioThread: Scheduler
    abstract val mainThread: Scheduler
}