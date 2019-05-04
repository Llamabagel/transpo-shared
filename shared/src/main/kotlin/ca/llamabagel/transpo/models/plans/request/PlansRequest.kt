/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.request

import ca.llamabagel.transpo.utils.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PlansRequest(val locations: List<Location>,
                        @Serializable(with = DateSerializer::class) val date: Date = Date(),
                        val allowBike: Boolean = false,
                        val requestTimeType: RequestTimeType = RequestTimeType.DEPART_AT
)