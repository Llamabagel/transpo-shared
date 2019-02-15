/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/app/gtfs/reference/#routestxt]
 */
data class Route(val id: RouteId,
                 val agencyId: AgencyId?,
                 val shortName: String,
                 val longName: String,
                 val description: String?,
                 val type: Int,
                 val url: String?,
                 val color: String?,
                 val textColor: String?,
                 val sortOrder: Int?)

inline class RouteId(val value: String)
fun String?.asRouteId() = if (this == null) null else RouteId(this)
