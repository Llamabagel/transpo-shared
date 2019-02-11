/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.request

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

data class PlansRequest(
        @SerializedName("locations") val locations: List<Location>,
        @SerializedName("date") val date: Date = Date(),
        @SerializedName("allowBike") val allowBike: Boolean = false,
        @SerializedName("requestTimeType") val requestTimeType: RequestTimeType = RequestTimeType.DEPART_AT
)