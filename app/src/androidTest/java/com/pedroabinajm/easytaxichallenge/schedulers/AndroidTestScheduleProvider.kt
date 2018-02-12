package com.pedroabinajm.easytaxichallenge.schedulers

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class AndroidTestScheduleProvider @Inject constructor() : ISchedulerProvider {

    override fun computation() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()
}