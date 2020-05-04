/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import ca.llamabagel.transpo.models.transit.Route
import ca.llamabagel.transpo.models.transit.RouteShape
import ca.llamabagel.transpo.models.transit.Stop
import ca.llamabagel.transpo.models.transit.StopRoute
import ca.llamabagel.transpo.utils.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

/**
 * The full package of transit data that is sent by the server when the client downloads a data set.
 *
 * @property dataVersion A string indicating the version of data contained in this package
 * @see Version
 * @property schemaVersion A string indicating what version of the schema this data is stored in
 * @property date The date at which this package was generated
 * @property data The actual data in the package
 * @see Data
 */
@Serializable
data class DataPackage(
        val dataVersion: String,
        val schemaVersion: Int,
        @Serializable(with = OffsetDateTimeSerializer::class) val date: OffsetDateTime,
        val data: Data
)

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
@Serializable
data class Data(
        val stops: List<Stop>,
        val routes: List<Route>,
        val stopRoutes: List<StopRoute>,
        val shapes: List<RouteShape>
)
