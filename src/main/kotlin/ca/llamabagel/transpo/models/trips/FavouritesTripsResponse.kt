/*
 * Copyright (c) 2018. Llamabagel
 *
 * This file is a part of Route 613.
 */

package ca.llamabagel.transpo.models.trips

import com.google.gson.annotations.SerializedName

data class FavouritesTripsResponse(@SerializedName("favourites") val favourites: List<Favourite>) {

    data class Favourite(@SerializedName("stopCode") val stopCode: String,
                         @SerializedName("stopId") val stopId: String,
                         @SerializedName("routes") val routes: List<FavouriteRoute>)

    data class FavouriteRoute(@SerializedName("routeNumber") val routeNumber: String,
                              @SerializedName("directionId") val directionId: Int,
                              @SerializedName("nextTrips") val nextTrips: List<Trip>,
                              @SerializedName("error") val error: Int)
}