/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.response

/**
 * Data class of the response to a request for travel plans.
 *
 * @property plans A list of [Plan] objects representing the travel plan options returned by the API.
 */
data class PlansResponse(val plans: List<Plan>)
