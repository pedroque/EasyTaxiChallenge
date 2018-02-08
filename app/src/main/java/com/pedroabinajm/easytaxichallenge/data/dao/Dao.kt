package com.pedroabinajm.easytaxichallenge.data.dao


interface Dao<T> {
    fun save(list: List<T>)
    fun save(data: T)
    fun findAll(): List<T>?
    fun find(): T?
    fun delete(data: T)
    fun delete()
}