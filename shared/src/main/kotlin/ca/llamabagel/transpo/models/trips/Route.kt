/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.trips

import kotlinx.serialization.Serializable

/**
 * Created by derek on 8/4/2017.
 * A bus route. Not the same as the other route. This is a route object returned from the trips API
 * which contains a list of trips for routes at a certain stop.
 * @property number The number of the route
 * @property directionId The id of the direction of travel
 * @property direction The direction of travel in words
 * @property heading The heading/destination of the route.
 */
@Serializable
data class Route(val number: String,
                 val directionId: Int,
                 val direction: String,
                 val heading: String,
                 val trips: List<Trip>)