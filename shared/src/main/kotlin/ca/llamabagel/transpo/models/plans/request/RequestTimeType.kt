/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.request

/**
 * Enum describing the type of request being made in relation to the specified time in the request.
 * * ARRIVE_AT The plan should arrive at the destination at the specified time.
 * * DEPART_AT The plan should depart the origin at the specified time.
 */
enum class RequestTimeType {
    ARRIVE_AT,
    DEPART_AT
}