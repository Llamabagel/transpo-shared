/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/app/gtfs/reference/#stopstxt]
 */
data class Stop(val id: StopId,
                val code: String?,
                val name: String,
                val description: String?,
                val latitude: Double,
                val longitude: Double,
                val zoneId: Int?,
                val stopUrl: String?,
                val locationType: Int?,
                val parentStation: String?,
                val timeZone: String?,
                val wheelchairBoarding: Int?) : GtfsObject() {
    companion object
}

inline class StopId(val value: String) {
    override fun toString(): String = value
}

fun String?.asStopId() = if (this == null) null else StopId(this)
