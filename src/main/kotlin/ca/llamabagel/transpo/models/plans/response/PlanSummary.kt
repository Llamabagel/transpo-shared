/*
 * Copyright (c) 2019 Llamabagel.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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