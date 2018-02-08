package com.pedroabinajm.easytaxichallenge.schedulers

import io.reactivex.Scheduler

interface ISchedulerProvider {
    fun computation() : Scheduler
    fun ui() : Scheduler
}