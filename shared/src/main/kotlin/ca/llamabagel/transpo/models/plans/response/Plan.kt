/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.response

data class Plan(val summary: PlanSummary,
                val steps: List<Step>)