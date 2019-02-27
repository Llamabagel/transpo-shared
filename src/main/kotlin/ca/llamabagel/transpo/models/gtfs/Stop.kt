/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

import ca.llamabagel.transpo.dao.impl.CsvHeader

/**
 * [https://developers.google.com/app/gtfs/reference/#stopstxt]
 */
data class Stop(@param:CsvHeader("stop_id") val id: StopId,
                @param:CsvHeader("stop_code") val code: String?,
                @param:CsvHeader("stop_name") val name: String,
                @param:CsvHeader("stop_desc") val description: String?,
                @param:CsvHeader("stop_lat") val latitude: Double,
                @param:CsvHeader("stop_lon") val longitude: Double,
                @param:CsvHeader("zone_id") val zoneId: Int?,
                @param:CsvHeader("stop_url") val stopUrl: String?,
                @param:CsvHeader("location_type") val locationType: Int?,
                @param:CsvHeader("parent_station") val parentStation: String?,
                @param:CsvHeader("time_zone") val timeZone: String?,
                @param:CsvHeader("wheelchair_boarding") val wheelchairBoarding: Int?) : GtfsObject() {
    companion object
}

inline class StopId(val value: String) {
    override fun toString(): String = value
}
fun String?.asStopId() = if (this == null) null else StopId(this)
