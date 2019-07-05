/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.updates

/**
 * Data class representing a stop that is affected by a service disruption.
 *
 * @property stop The 4-digit code of the stop that is affected
 * @property alternateService The 4-digit code of the stop where alternate service will be provided, if any
 */
data class AffectedStop(val stop: String,
                        val alternateService: String? = null)