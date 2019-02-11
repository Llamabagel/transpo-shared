/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.response

import com.google.gson.annotations.SerializedName

/**
 * Data class of the response to a request for travel plans.
 *
 * @property plans A list of [Plan] objects representing the travel plan options returned by the API.
 * @property error An integer error code (which is just the ordinal value of an entry in [PlansResponseError]) indicating
 * any errors that may have occurred when processing the request.
 * @property errorMessage A text description of the error that occurred.
 */
data class PlansResponse(@SerializedName("plans") val plans: List<Plan>,
                         @SerializedName("error") val error: Int = PlansResponseError.OK.ordinal,
                         @SerializedName("errorMessage") val errorMessage: String = PlansResponseError.OK.message) {

    constructor(plans: List<Plan>, error: PlansResponseError) : this(plans, error.ordinal, error.message)

    /**
     * Convenience method to get the [PlansResponseError] entry corresponding to the error code stored in the object.
     *
     * @return The [PlansResponseError] corresponding to the ordinal value of [error].
     */
    fun getError(): PlansResponseError = PlansResponseError.values()[error]

}

enum class PlansResponseError(val message: String) {
    OK(""),
    INVALID_ORIGIN("Invalid origin specified."),
    INVALID_DESTINATION("Invalid destination specified."),
    INVALID_WAYPOINT("Invalid waypoint specified."),
    NO_PLANS("No plans found for the requested locations.")
}