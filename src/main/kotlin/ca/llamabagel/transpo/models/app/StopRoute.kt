/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by isaac on 7/19/2017.
 * @property stop The stop for this stop route
 * @property routeId The id of the route that passes through the stop.
 * @property directionId the direction that the route is travelling (either 0 or 1)
 */
@Entity(tableName = "stop_routes", primaryKeys = ["stop", "routeId", "directionId"])
data class StopRoute(@ColumnInfo(name = "stopId") @SerializedName("stopId") val stop: String,
                     @ColumnInfo(name = "routeId") @SerializedName("routeId") val routeId: String,
                     @ColumnInfo(name = "directionId") @SerializedName("directionId") val directionId: Int)