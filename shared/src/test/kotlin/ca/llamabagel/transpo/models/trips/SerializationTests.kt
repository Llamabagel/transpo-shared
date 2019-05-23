/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.trips

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import org.junit.Assert.assertEquals
import org.junit.Test

class SerializationTests {
    private val gpsTrip = Trip("Destination", "11:32", 5, 0.63f, false, "L", true, 0, 1.11, 2.22, 1.23f)
    private val noGpsTrip = Trip("No GPS!", "10:30", 6, -1f, false, "S", false, 0)
    private val route = Route("613", 1, "Outbound", "Somewhere", listOf(gpsTrip, noGpsTrip))

    private val apiResponse = ApiResponse("1234", listOf(route))

    @UnstableDefault
    @ImplicitReflectionSerializer
    @Test
    fun testApiResponseSerialization() {
        val serialized = Json.stringify(apiResponse)

        // This is essentially guaranteed to parse the json correctly, however it could throw exceptions which is what we're really looking for.
        val deserialized = Json.parse(ApiResponse.serializer(), serialized)

        assertEquals(apiResponse.stopCode, deserialized.stopCode)
    }
}