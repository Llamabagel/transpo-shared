/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import ca.llamabagel.transpo.models.LatLng
import ca.llamabagel.transpo.models.app.EncodedShapeData
import org.junit.Assert.assertEquals
import org.junit.Test

class LatLngUtilsTests {
    private val latLngList = listOf(LatLng(45.42084, -75.70014),
            LatLng(45.42199, -75.69752),
            LatLng(45.42330, -75.69469),
            LatLng(45.42481, -75.69486),
            LatLng(45.42611, -75.69199))

    @Test
    fun testEncode() {
        assertEquals("ggftGzd`mMeFkOeGuPmH`@cG}P", latLngList.encode().data)
    }

    @Test
    fun testDecode() {
        val decoded = EncodedShapeData("ggftGzd`mMeFkOeGuPmH`@cG}P").decode()
        val maxDelta = 0.000001

        assertEquals(latLngList[0].latitude, decoded[0].latitude, maxDelta)
        assertEquals(latLngList[0].longitude, decoded[0].longitude, maxDelta)

        assertEquals(latLngList[1].latitude, decoded[1].latitude, maxDelta)
        assertEquals(latLngList[1].longitude, decoded[1].longitude, maxDelta)

        assertEquals(latLngList[2].latitude, decoded[2].latitude, maxDelta)
        assertEquals(latLngList[2].longitude, decoded[2].longitude, maxDelta)

        assertEquals(latLngList[3].latitude, decoded[3].latitude, maxDelta)
        assertEquals(latLngList[3].longitude, decoded[3].longitude, maxDelta)

        assertEquals(latLngList[4].latitude, decoded[4].latitude, maxDelta)
        assertEquals(latLngList[4].longitude, decoded[4].longitude, maxDelta)
    }
}