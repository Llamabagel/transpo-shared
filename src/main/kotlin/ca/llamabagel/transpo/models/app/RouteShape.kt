/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName


/**
 * The shape of some given route. Shape data is stored as an encoded polyline string.
 *
 * @property routeId The id of the route
 * @property shapeId An id identifying this particular shape for the given route
 * @property shapeData The encoded polyline data.
 */
@Entity(tableName = "route_shapes", primaryKeys = ["routeId", "shapeId"],
        foreignKeys = [ForeignKey(entity = Route::class, parentColumns = ["id"], childColumns = ["routeId"])])
data class RouteShape(@ColumnInfo(name = "routeId") @SerializedName("routeId") val routeId: String,
                      @ColumnInfo(name = "shapeId") @SerializedName("shapeId") val shapeId: String,
                      @ColumnInfo(name = "shapeData") @SerializedName("shapeData") val shapeData: String)
