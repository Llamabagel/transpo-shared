/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.trips

import ca.llamabagel.transpo.utils.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

/**
 * API response from the Route 613 API computed from the response of an OC Transpo API request.
 *
 * @property stopCode The 4-digit stop code of the stop of which this response is about.
 * @property routes A list of [Route]s that serve this stop.
 * @property inactiveRoutes A list of [Route]s that serve this stop but _do not_ have any trips at this time.
 * @property responseTime The time at which this response was generated in Unix time.
 */
@Serializable
data class ApiResponse(val stopCode: String,
                       val routes: List<Route>,
                       val inactiveRoutes: List<Route> = emptyList(),
                       @Serializable(with = OffsetDateTimeSerializer::class) val responseTime: OffsetDateTime = OffsetDateTime.now())