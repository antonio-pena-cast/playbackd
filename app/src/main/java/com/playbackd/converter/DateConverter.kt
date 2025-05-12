package com.playbackd.converter

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate

class DateConverter : JsonDeserializer<LocalDate> {
    @RequiresApi(VERSION_CODES.O)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate? {
        val s = json!!.getAsJsonPrimitive().asString
        val date = LocalDate.parse(s)
        return date
    }
}
