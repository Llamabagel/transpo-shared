/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.response

import com.google.gson.annotations.SerializedName

data class Plan(@SerializedName("summary") val summary: PlanSummary,
                @SerializedName("steps") val steps: List<Step>)