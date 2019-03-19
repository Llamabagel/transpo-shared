/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

@file:JvmName("LatLngUtils")

package ca.llamabagel.transpo.utils

import ca.llamabagel.transpo.models.LatLng

/**
 * Encodes a list of LatLngs into an encoded path string
 */
fun List<LatLng>.encode(): String {
    var lastLat = 0L
    var lastLng = 0L

    val result = StringBuffer()

    for (point in this) {
        val lat = Math.round(point.latitude * 1e5)
        val lng = Math.round(point.longitude * 1e5)

        val dLat = lat - lastLat
        val dLng = lng - lastLng

        encode(dLat, result)
        encode(dLng, result)

        lastLat = lat
        lastLng = lng
    }

    return result.toString()
}

/**
 * Decodes an encoded path string into a list of LatLngs
 */
fun String.decode(): List<LatLng> {
    val length = this.length

    val path = mutableListOf<LatLng>()
    var index = 0
    var lat = 0
    var lng = 0

    while (index < length) {
        var result = 1
        var shift = 0
        var b: Int
        do {
            b = this[index++].toInt() - 63 - 1
            result += b shl shift
            shift += 5
        } while (b >= 0x1F)
        lat += if (result and 1 != 0) (result shr 1).inv() else result shr 1

        result = 1
        shift = 0
        do {
            b = this[index++].toInt() - 63 - 1
            result += b shl shift
            shift += 5
        } while (b >= 0x1F)
        lng += if (result and 1 != 0) (result shr 1).inv() else result shr 1

        path.add(LatLng(lat * 1e-5, lng * 1e-5))
    }

    return path
}

private fun encode(v: Long, result: StringBuffer) {
    var x = if (v < 0) (v shl 1).inv() else v shl 1
    while (x >= 0x20) {
        result.append(Character.toChars(((0x20L or (x and 0x1FL)) + 63L).toInt()))

        x = x shr 5
    }
    result.append(Character.toChars((x + 63L).toInt()))
}
