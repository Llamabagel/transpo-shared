/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Data about a Route derived from GTFS data but simplified for in-client use.
 *
 * @property id The unique id string for this route.
 * @property shortName A short name for this route (typically just the route number)
 * @property longName The long name of the route (usually combined headings?)
 * @property type The route's type, i.e. rail, bus. 2 indicates rail, 3 indicates bus.
 * @property serviceLevel The level of service, or how the route is branded (frequent, rapid, local, etc.)
 * @property color The colour to be used to display this route
 */
@Entity(tableName = "routes")
data class Route(
        @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: String,
        @ColumnInfo(name = "shortName") @SerializedName("shortName") val shortName: String,
        @ColumnInfo(name = "longName") @SerializedName("longName") val longName: String?,
        @ColumnInfo(name = "type") @SerializedName("type") val type: Int,
        @ColumnInfo(name = "serviceLevel") @SerializedName("serviceLevel") val serviceLevel: String,
        @ColumnInfo(name = "color") @SerializedName("color") val color: String
)