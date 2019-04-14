package com.grosslicht.burgertuesday.serialization

import at.favre.lib.idmask.IdMask
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.beans.factory.annotation.Autowired

class IdMaskSerializer : StdSerializer<Long>(Long::class.java) {
    @Autowired
    private lateinit var idMask: IdMask<Long>

    override fun serialize(value: Long, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(idMask.mask(value))
    }
}

class IdMaskDeserializer : StdDeserializer<Long>(Long::class.java) {
    @Autowired
    private lateinit var idMask: IdMask<Long>

    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Long {
        val id = p?.readValueAs(String::class.java)
        return idMask.unmask(id)
    }
}
