/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import kotlinx.serialization.Serializable

/**
 * Created by isaac on 7/19/2017.
 *
 * @property stop The stop for this stop route
 * @property routeId The id of the route that passes through the stop.
 * @property directionId the direction that the route is travelling (either 0 or 1)
 * @property sequence An integer representing the position of this in the sequence of stops along the route.
 * This is a general sequence across the entire route and does not account for branches or individual trips. To get the
 * sequencing of stops along a specific trip on a route, use a Trip object instead.
 */
@Serializable
data class StopRoute(val stop: String,
                     val routeId: String,
                     val directionId: Int,
                     val sequence: Int)