/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models

import com.google.gson.annotations.SerializedName

/**
 * Encapsulated WGS 84 Latitude and Longitude point.
 *
 * @property latitude WGS 84 latitude (between -180 and 180)
 * @property longitude WGS 84 longitude (between -180 and 180)
 */
data class LatLng(@SerializedName("latitude") var latitude: Double,
                  @SerializedName("longitude") var longitude: Double)