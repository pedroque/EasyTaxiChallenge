package com.pedroabinajm.easytaxichallenge.data.entity.mapper

import com.pedroabinajm.easytaxichallenge.data.entity.BookmarkEntity
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace


interface BookmarkMapper {
    fun transform(bookmarkEntity: BookmarkEntity): EasyPlace
}