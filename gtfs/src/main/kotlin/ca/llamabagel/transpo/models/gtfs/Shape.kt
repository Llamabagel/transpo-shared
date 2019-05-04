/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/transit/gtfs/reference/#shapestxt]
 */
data class Shape(val id: ShapeId,
                 val latitude: Double,
                 val longitude: Double,
                 val sequence: Int,
                 val distanceTraveled: Double?) : GtfsObject() {
    companion object
}


inline class ShapeId(val value: String)
fun String?.asShapeId() = if (this == null) null else ShapeId(this)
