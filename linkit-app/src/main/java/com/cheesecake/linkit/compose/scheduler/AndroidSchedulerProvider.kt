package com.cheesecake.linkit.compose.scheduler

import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidSchedulerProvider : SchedulerProvider() {
    override val ioThread: Scheduler = Schedulers.io()
    override val mainThread: Scheduler = AndroidSchedulers.mainThread()
}