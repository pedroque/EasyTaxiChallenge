package com.pedroabinajm.easytaxichallenge.schedulers

import io.reactivex.schedulers.Schedulers


class TestSchedulerProvider : ISchedulerProvider {

    override fun computation() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()
}