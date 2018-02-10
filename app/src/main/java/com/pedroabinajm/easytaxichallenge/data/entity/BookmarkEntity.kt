package com.pedroabinajm.easytaxichallenge.data.entity

import com.google.gson.annotations.Expose


class BookmarkEntity constructor(@Expose
                                 val name: String,
                                 @Expose
                                 val latitude: Double,
                                 @Expose
                                 val longitude: Double)