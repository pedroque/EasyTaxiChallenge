package com.pedroabinajm.easytaxichallenge.data.entity

import com.google.gson.annotations.Expose


class BookmarkResponseEntity constructor(@Expose
                                         val favorites: List<BookmarkEntity>)