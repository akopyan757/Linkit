package com.cheesecake.linkit.compose.scheduler

import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class AndroidMainSchedulerProvider : SchedulerProvider() {
    override val ioThread: Scheduler = AndroidSchedulers.mainThread()
    override val mainThread: Scheduler = AndroidSchedulers.mainThread()
}