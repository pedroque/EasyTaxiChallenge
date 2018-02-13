package com.pedroabinajm.easytaxichallenge.data.entity.mapper

import com.pedroabinajm.easytaxichallenge.data.entity.BookmarkEntity
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import java.util.*
import javax.inject.Inject


class BookmarkMapperImpl @Inject constructor() : BookmarkMapper {
    override fun transform(bookmarkEntity: BookmarkEntity): EasyPlace {
        return EasyPlace(
                UUID.randomUUID().toString(),
                bookmarkEntity.name,
                bookmarkEntity.latitude,
                bookmarkEntity.longitude,
                null,
                true
        )
    }
}