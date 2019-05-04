/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.request

import ca.llamabagel.transpo.models.LatLng
import ca.llamabagel.transpo.models.transit.Stop
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable


/**
 * A location being referenced in a travel plan request. These classes are used by the travel plan API to find travel
 * plans between the listed locations. The different types of locations the API accepts are listed below as the children
 * of this class. This class can't be instantiated directly (it is sealed) so the children classes should be used.
 *
 * @property description An optional name/description of the location.
 */
@Serializable
sealed class Location(val description: String?) {

    /**
     * A location defined as a "place" identified by its placeId as specified in the Google Places API.
     *
     * @property placeId This location's placeId from the Places API.
     */
    @Serializable(with = PolymorphicSerializer::class)
    class PlaceLocation(val placeId: String, description: String?) : Location(description)

    /**
     * A location defined by a latitude and longitude (WGS84).
     *
     * @property latLng This location's latitude and longitude.
     */
    @Serializable(with = PolymorphicSerializer::class)
    class LatLngLocation(val latLng: LatLng, description: String?) : Location(description)

    /**
     * A location defined as a stop in the transit system.
     *
     * @property stop The stop.
     */
    @Serializable(with = PolymorphicSerializer::class)
    class StopLocation(val stop: Stop) : Location(stop.name)
}
