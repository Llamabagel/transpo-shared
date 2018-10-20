/*
 * Copyright (c) 2018. Llamabagel
 *
 * This file is a part of Route 613.
 */

package ca.llamabagel.transpo.models

import ca.llamabagel.transpo.models.app.Route
import ca.llamabagel.transpo.models.app.RouteMap
import ca.llamabagel.transpo.models.app.Stop
import ca.llamabagel.transpo.models.app.StopRoute
import com.google.gson.annotations.SerializedName

/**
 * Response model for the data download API call.
 * @property stops List of Stops
 * @see Stop
 * @property routes List of Routes
 * @see Route
 * @property stopRoutes List of StopRoutes
 * @see StopRoute
 * @property routeMaps List of RouteMaps
 * @see RouteMap
 */
data class EssentialResponse(
        @SerializedName("stops") val stops: ArrayList<Stop>,
        @SerializedName("routes") val routes: ArrayList<Route>,
        @SerializedName("stopRoutes") val stopRoutes: ArrayList<StopRoute>,
        @SerializedName("routeMaps") val routeMaps: ArrayList<RouteMap>
)