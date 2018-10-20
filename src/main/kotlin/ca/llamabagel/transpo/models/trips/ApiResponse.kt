/*
 * Copyright (c) 2017. Llamabagel
 *
 * This file is a part of Route 613.
 */

package ca.llamabagel.transpo.models.trips

import ca.llamabagel.transpo.models.trips.Route
import com.google.gson.annotations.SerializedName

/**
 * Created by derek on 8/5/2017.
 * API response encapsulation from OUR API.
 */
data class ApiResponse(
        @SerializedName("stopCode") val stopCode: String,
        @SerializedName("error") val error: Int,
        @SerializedName("routes") val routes: ArrayList<Route>
)