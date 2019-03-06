/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import ca.llamabagel.transpo.utils.encode
import ca.llamabagel.transpo.utils.decode
import com.google.gson.annotations.SerializedName


/**
 * The shape of some given route. Shape data is stored as an encoded polyline string.
 *
 * @property routeId The id of the route
 * @property shapeId An id identifying this particular shape for the given route
 * @property shapeData The encoded polyline data.
 * @see EncodedShapeData
 */
@Entity(tableName = "route_shapes", primaryKeys = ["routeId", "shapeId"])
data class RouteShape(@ColumnInfo(name = "routeId") @SerializedName("routeId") val routeId: String,
                      @ColumnInfo(name = "shapeId") @SerializedName("shapeId") val shapeId: String,
                      @ColumnInfo(name = "shapeData") @SerializedName("shapeData") val shapeData: EncodedShapeData)

/**
 * Inline class wrapper around a string for encoded polyline string data.
 * Can be [decode]d using util function, and can be created by [encode]-ing a list of LatLngs.
 */
inline class EncodedShapeData(val data: String)