package com.pedroabinajm.easytaxichallenge.data.net

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateTypeAdapter @Inject constructor() : TypeAdapter<Date>() {

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Date? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        return deserializeToDate(reader.nextString())
    }

    @Synchronized
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Date?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(serializeDate(value))
    }

    private fun deserializeToDate(s: String): Date? {
        return try {
            getFormatter().parse(s)
        } catch (e: ParseException) {
            null
        }

    }

    private fun serializeDate(value: Date): String? {
        return try {
            getFormatter().format(value)
        } catch (e: ParseException) {
            null
        }
    }

    private fun getFormatter() : SimpleDateFormat{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = SimpleTimeZone(0, "UTC")
        return sdf
    }

}
