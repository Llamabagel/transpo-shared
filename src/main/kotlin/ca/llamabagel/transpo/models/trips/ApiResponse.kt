/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.trips

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * API response from the Route 613 API computer from the response of an OC Transpo API request.
 *
 * For [error] the following values are possible:
 * * 0 = OK
 * * 1 = Invalid API Key
 * * 2 = Unable to query data source
 * * 10 = Invalid stop number
 * * 11 = Invalid route number
 * * 12 = Stop does not service route
 *
 * @property stopCode The 4-digit stop code of the stop of which this response is about.
 * @property error Any error messages reported by the OC Transpo API. See above for possible values.
 * @property routes A list of [Route]s that serve this stop.
 * @property inactiveRoutes A list of [Route]s that serve this stop but _do not_ have any trips at this time.
 * @property responseTime The time at which this response was generated in Unix time.
 */
data class ApiResponse(
        @SerializedName("stopCode") val stopCode: String,
        @SerializedName("error") val error: Int,
        @SerializedName("routes") val routes: List<Route>,
        @SerializedName("inactiveRoutes") val inactiveRoutes: List<Route> = emptyList(),
        @SerializedName("responseTime") val responseTime: Long = Date().time
)