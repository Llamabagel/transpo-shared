/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private val df: DateFormat = SimpleDateFormat("MMM dd, yyyy h:mm:ss aa")

    override val descriptor: SerialDescriptor =
            StringDescriptor.withName("WithCustomDefault")

    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeString(df.format(obj))
    }

    override fun deserialize(decoder: Decoder): Date {
        return df.parse(decoder.decodeString())
    }
}