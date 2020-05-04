/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import kotlinx.serialization.Serializable


/**
 * The shape of some given route. Shape data is stored as an encoded polyline string.
 *
 * @property routeId The id of the route
 * @property shapeId An id identifying this particular shape for the given route
 * @property shapeData The encoded polyline data.
 */
@Serializable
data class RouteShape(
        val routeId: String,
        val shapeId: String,
        val shapeData: String
)
