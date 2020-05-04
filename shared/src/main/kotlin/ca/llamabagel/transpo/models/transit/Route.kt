/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import kotlinx.serialization.Serializable

/**
 * Data about a Route derived from GTFS data but simplified for in-client use.
 *
 * @property id The unique id string for this route.
 * @property shortName A short name for this route (typically just the route number)
 * @property longName The long name of the route (usually combined headings?)
 * @property type The route's type, i.e. rail, bus. 2 indicates rail, 3 indicates bus.
 * @property serviceLevel The level of service, or how the route is branded (frequent, rapid, local, etc.)
 * @property color The colour to be used to display this route
 */
@Serializable
data class Route(
        val id: String,
        val shortName: String,
        val longName: String?,
        val type: Int,
        val serviceLevel: String,
        val color: String
)
