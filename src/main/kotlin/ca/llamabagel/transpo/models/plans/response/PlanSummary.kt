/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.response

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * A summary of a travel plan.
 *
 * @property startTime The time at which the travel plan would begin
 * @property endTime The time at which the travel plan would end
 * @property travelTime The elapsed time between the start and end times in minutes (i.e. how long this travel plan takes)
 * @property travelWalkTime How much walking is required in minutes
 */
data class PlanSummary(@SerializedName("startTime") val startTime: Date,
                       @SerializedName("endTime") val endTime: Date,
                       @SerializedName("travelTime") val travelTime: Int,
                       @SerializedName("travelWalkTime") val travelWalkTime: Int)