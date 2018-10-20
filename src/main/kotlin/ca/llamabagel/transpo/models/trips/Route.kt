/*
 * Copyright (c) 2017. Llamabagel
 *
 * This file is a part of Route 613.
 */

package ca.llamabagel.transpo.models.trips

import com.google.gson.annotations.SerializedName

/**
 * Created by derek on 8/4/2017.
 * A bus route. Not the same as the other route. This is a route object returned from the trips API
 * which contains a list of trips for routes at a certain stop.
 * @property number The number of the route
 * @property directionId The id of the direction of travel
 * @property direction The direction of travel in words
 * @property heading The heading/destination of the route.
 */
data class Route(
        @SerializedName("number") val number: String,
        @SerializedName("directionId") val directionId: Int,
        @SerializedName("direction") val direction: String,
        @SerializedName("heading") val heading: String,
        @SerializedName("trips") val trips: List<Trip>
)