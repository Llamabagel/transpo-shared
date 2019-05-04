/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import ca.llamabagel.transpo.models.app.Version
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private val df: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS")

    override val descriptor: SerialDescriptor =
            StringDescriptor.withName("WithCustomDefault")

    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeString(df.format(obj))
    }

    override fun deserialize(decoder: Decoder): Date {
        return df.parse(decoder.decodeString())
    }
}

@Serializer(forClass = Version::class)
object VersionSerializer : KSerializer<Version> {

    override fun serialize(encoder: Encoder, obj: Version) {
        encoder.encodeString(obj.value)
    }

    override fun deserialize(decoder: Decoder): Version {
        return Version(decoder.decodeString())
    }

}