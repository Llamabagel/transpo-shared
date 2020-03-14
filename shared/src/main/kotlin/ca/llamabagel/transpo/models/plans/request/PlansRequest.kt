/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.request

import ca.llamabagel.transpo.utils.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class PlansRequest(val locations: List<Location>,
                        @Serializable(with = OffsetDateTimeSerializer::class) val date: OffsetDateTime = OffsetDateTime.now(),
                        val allowBike: Boolean = false,
                        val requestTimeType: RequestTimeType = RequestTimeType.DEPART_AT
)