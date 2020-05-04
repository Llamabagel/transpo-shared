/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import kotlinx.serialization.Serializable

@Serializable
data class StopTime(
        val id: String,
        val arrivalTime: String,
        val departureTime: String,
        val stopId: String,
        val stopSequence: Int,
        val pickupType: Int,
        val dropOffType: Int
)
