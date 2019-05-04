/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models

import kotlinx.serialization.Serializable

/**
 * Encapsulated WGS 84 Latitude and Longitude point.
 *
 * @property latitude WGS 84 latitude (between -180 and 180)
 * @property longitude WGS 84 longitude (between -180 and 180)
 */
@Serializable
data class LatLng(val latitude: Double,
                  val longitude: Double)