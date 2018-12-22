/*
 * Copyright (c) 2018 Llamabagel.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.llamabagel.transpo.models.plans.request

import ca.llamabagel.transpo.models.LatLng
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type


sealed class Location(@SerializedName("description") val description: String) {

    class PlaceLocation(description: String, @SerializedName("identifier") val identifier: String) : Location(description)

    class LatLngLocation(description: String, @SerializedName("latLng") val latLng: LatLng) : Location(description)

    class StopLocation(description: String, @SerializedName("latLng") val latLng: LatLng,
                       @SerializedName("id") val id: String) : Location(description)

}

class LocationTypeAdapter : TypeAdapter<Location>() {
    override fun write(out: JsonWriter, value: Location) {

        out.beginObject()
        when (value) {
            is Location.PlaceLocation -> {
                out.name("type").value("place")
                out.name("identifier").value(value.identifier)
            }
            is Location.LatLngLocation -> {
                out.name("type").value("latLng")
                writeLatLng(value.latLng, out)
            }
            is Location.StopLocation -> {
                out.name("type").value("stop")
                writeLatLng(value.latLng, out)
                out.name("id").jsonValue(value.id)
            }
        }
        out.name("description").value(value.description)


        out.endObject()
    }

    override fun read(reader: JsonReader): Location {
        reader.beginObject()

        // Get the type to determine which child of the sealed Location class to instantiate
        reader.nextName()
        when (reader.nextString()) {
            "place" -> {
                reader.nextName()
                val identifier = reader.nextString()
                reader.nextName()
                val description = reader.nextString()
                return Location.PlaceLocation(description, identifier)
            }
            "latLng" -> {
                reader.nextName()
                val latLng = readLatLng(reader)
                reader.nextName()
                val description = reader.nextString()
                return Location.LatLngLocation(description, latLng)
            }
            "stop" -> {
                reader.nextName()
                val latLng = readLatLng(reader)
                reader.nextName()
                val id = reader.nextString()
                reader.nextName()
                val description = reader.nextString()
                return Location.StopLocation(description, latLng, id)
            }
        }

        return Location.PlaceLocation("", "")
    }

    /**
     * Writes a [LatLng] object to JSON without using an additional [Gson] object.
     * @param latLng The [LatLng] object to serialize
     * @param writer The [JsonWriter] to write the object to
     */
    private fun writeLatLng(latLng: LatLng, writer: JsonWriter) {
        writer.name("latLng").beginObject()
                .name("latitude").value(latLng.latitude)
                .name("longitude").value(latLng.longitude)
                .endObject()
    }

    /**
     * Reads a [LatLng] object from a [JsonReader]. This assumes that the next token in the reader will be the
     * beginning of the LatLng object.
     *
     * @param reader The reader to read the LatLng in from
     * @return A LatLng object.
     */
    private fun readLatLng(reader: JsonReader): LatLng {
        reader.beginObject()
        reader.nextName()
        val latitude = reader.nextDouble()
        reader.nextName()
        val longitude = reader.nextDouble()
        reader.endObject()

        return LatLng(latitude, longitude)
    }
}
