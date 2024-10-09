package com.luminsoft.enroll_sdk.innovitices.ui

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ByteArraySerializer : JsonSerializer<ByteArray> {

    override fun serialize(
        src: ByteArray?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive("${src?.size} bytes")
    }
}
