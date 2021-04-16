package com.akopyan757.linkit.common

import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidMainSchedulerProvider : SchedulerProvider() {
    override val ioThread: Scheduler = AndroidSchedulers.mainThread()
    override val mainThread: Scheduler = AndroidSchedulers.mainThread()
}