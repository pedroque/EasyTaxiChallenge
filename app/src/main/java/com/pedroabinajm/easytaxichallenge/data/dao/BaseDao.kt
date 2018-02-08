package com.pedroabinajm.easytaxichallenge.data.dao

import io.realm.Realm
import io.realm.RealmObject

abstract class BaseDao<T : RealmObject>(internal var mClass: Class<T>) : Dao<T> {

    override fun save(list: List<T>) {
        Realm.getDefaultInstance().use { realm ->
            realm!!.executeTransaction { it.insertOrUpdate(list) }
        }
    }

    override fun save(data: T) {
        Realm.getDefaultInstance().use { realm ->
            realm!!.executeTransaction { it.insertOrUpdate(data) }
        }
    }

    override fun findAll(): List<T>? {
        Realm.getDefaultInstance().use { realm ->
            val result = realm.where(mClass).findAll()
            return if (result == null) {
                null
            } else {
                realm.copyFromRealm(result)
            }
        }
    }

    override fun find(): T? {
        Realm.getDefaultInstance().use { realm ->
            val result = realm!!.where(mClass).findFirst()
            return if (result == null) {
                null
            } else {
                realm.copyFromRealm(result)
            }
        }
    }

    override fun delete(data: T) {
        data.deleteFromRealm()
    }

    override fun delete() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { it.delete(mClass) }
        }
    }
}
