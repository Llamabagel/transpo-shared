/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import ca.llamabagel.transpo.models.LatLng
import kotlinx.serialization.Serializable

/**
 * A stop object. Represents a station, bus stop, train station, etc.
 *
 * @property id The unique id string for this stop.
 * @property code The code for this stop that is used by passengers.
 * @property name The name of this stop.
 * @property latitude The latitude of this stop in WGS 84.
 * @property longitude The longitude of this stop in WGS 84.
 * @property locationType The type of stop this is. 0 indicates a regular stop, 1 indicates a station
 * @property parentStation If this stop is indicated as a station, it could have multiple stops or platforms within it.
 * This references another stop's id.
 */
@Serializable
data class Stop(
        val id: String,
        val code: String,
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val locationType: Int,
        val parentStation: String?
) {

    fun getLocation(): LatLng = LatLng(latitude, longitude)
}
