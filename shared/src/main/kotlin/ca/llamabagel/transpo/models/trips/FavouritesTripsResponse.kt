/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.trips


data class FavouritesTripsResponse(val favourites: List<Favourite>) {

    data class Favourite(val stopCode: String,
                         val stopId: String,
                         val routes: List<FavouriteRoute>)

    data class FavouriteRoute(val routeNumber: String,
                              val directionId: Int,
                              val nextTrips: List<Trip>,
                              val error: Int)
}