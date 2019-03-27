/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName

/**
 * Created by isaac on 7/19/2017.
 *
 * @property stop The stop for this stop route
 * @property routeId The id of the route that passes through the stop.
 * @property directionId the direction that the route is travelling (either 0 or 1)
 * @property sequence An integer representing the position of this in the sequence of stops along the route.
 * This is a general sequence across the entire route and does not account for branches or individual trips. To get the
 * sequencing of stops along a specific trip on a route, use a Trip object instead.
 */
@Entity(tableName = "stop_routes", primaryKeys = ["stopId", "routeId", "directionId"],
        foreignKeys = [
            ForeignKey(entity = Stop::class, parentColumns = ["id"], childColumns = ["stopId"], onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = Route::class, parentColumns = ["id"], childColumns = ["routeId"], onDelete = ForeignKey.CASCADE)
        ])
data class StopRoute(@ColumnInfo(name = "stopId", index = true) @SerializedName("stopId") val stop: String,
                     @ColumnInfo(name = "routeId", index = true) @SerializedName("routeId") val routeId: String,
                     @ColumnInfo(name = "directionId") @SerializedName("directionId") val directionId: Int,
                     @ColumnInfo(name = "sequence") @SerializedName("sequence") val sequence: Int)