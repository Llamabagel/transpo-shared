/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.request

import ca.llamabagel.transpo.models.LatLng
import ca.llamabagel.transpo.models.app.Stop
import com.google.gson.annotations.SerializedName
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory


/**
 * A location being referenced in a travel plan request. These classes are used by the travel plan API to find travel
 * plans between the listed locations. The different types of locations the API accepts are listed below as the children
 * of this class. This class can't be instantiated directly (it is sealed) so the children classes should be used.
 *
 * @property description An optional name/description of the location.
 */
sealed class Location(@SerializedName("description") val description: String?) {

    /**
     * A location defined as a "place" identified by its placeId as specified in the Google Places API.
     *
     * @property placeId This location's placeId from the Places API.
     */
    class PlaceLocation(@SerializedName("placeId") val placeId: String, description: String?) : Location(description)

    /**
     * A location defined by a latitude and longitude (WGS84).
     *
     * @property latLng This location's latitude and longitude.
     */
    class LatLngLocation(@SerializedName("latLng") val latLng: LatLng, description: String?) : Location(description)

    /**
     * A location defined as a stop in the transit system.
     *
     * @property stop The stop.
     */
    class StopLocation(@SerializedName("stop") val stop: Stop) : Location(stop.name)

}

/**
 * TypeAdapterFactory for serializing and deserializing [Location] objects.
 * @see Location
 */
val locationTypeFactory: RuntimeTypeAdapterFactory<Location> = RuntimeTypeAdapterFactory.of(Location::class.java, "type")
        .registerSubtype(Location.PlaceLocation::class.java, "place")
        .registerSubtype(Location.LatLngLocation::class.java, "latLng")
        .registerSubtype(Location.StopLocation::class.java, "stop")