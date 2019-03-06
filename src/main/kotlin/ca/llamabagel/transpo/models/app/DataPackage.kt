/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * The full package of app data that is sent by the server when the client downloads a data set.
 *
 * @property dataVersion A string indicating the version of data contained in this package
 * @property schemaVersion A string indicating what version of the scehma this data is stored in
 * @property date The date at which this package was generated
 * @property data The actual data in the package
 * @see Data
 */
data class DataPackage(@SerializedName("dataVersion") val dataVersion: String,
                       @SerializedName("schemaVersion") val schemaVersion: Int,
                       @SerializedName("date") val date: Date,
                       @SerializedName("data") val data: Data)

/**
 * Data contained in a data package
 *
 * @property stops List of all stop data.
 * @see Stop
 *
 * @property routes List of all route data
 * @see Route
 *
 * @property stopRoutes List of all routes and the stops they serve
 * @see StopRoute
 *
 * @property shapes List of all route shape data
 * @see RouteShape
 */
data class Data(@SerializedName("stops") val stops: List<Stop>,
                @SerializedName("routes") val routes: List<Route>,
                @SerializedName("stopRoutes") val stopRoutes: List<StopRoute>,
                @SerializedName("shapes") val shapes: List<RouteShape>)