package com.pedroabinajm.easytaxichallenge.data.dao

import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.realm.Realm


class PlaceDaoImpl : BaseDao<EasyPlace>(EasyPlace::class.java), PlaceDao {
    override fun delete(id: String) {
        Realm.getDefaultInstance().use { realm ->
            findFromRealm(realm, id)?.deleteFromRealm()
        }
    }

    override fun find(id: String): EasyPlace? {
        Realm.getDefaultInstance().use { realm ->
            return findFromRealm(realm, id)?.let { realm.copyFromRealm(it) }
        }
    }

    private fun findFromRealm(realm: Realm, id: String): EasyPlace? {
        return realm.where(mClass)
                .equalTo("id", id)
                .findFirst()
    }
}