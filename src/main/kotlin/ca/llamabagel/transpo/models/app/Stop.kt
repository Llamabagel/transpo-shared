/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ca.llamabagel.transpo.models.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * A stop object. Represents a station, bus stop, train station, etc.
 *
 * @property id The unique id string for this stop.
 * @property code The code for this stop that is used by passengers.
 * @property name The name of this stop.
 * @property latitude The latitude of this stop in WGS 84.
 * @property longitude The longitude of this stop in WGS 84.
 * @property locationType The type of stop this is. 0 indicates a regular stop, 1 indicates a station
 * @property parentStation If this stop is indicated as a station, it could have multiple stops or platforms within it.
 * This references another stop's id.
 */
@Entity(tableName = "stops")
data class Stop(@PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: String,
                @ColumnInfo(name = "code") @SerializedName("code") val code: String,
                @ColumnInfo(name = "name") @SerializedName("name") val name: String,
                @ColumnInfo(name = "latitude") @SerializedName("latitude") val latitude: Double,
                @ColumnInfo(name = "longitude") @SerializedName("longitude") val longitude: Double,
                @ColumnInfo(name = "locationType") @SerializedName("locationType") val locationType: Int,
                @ColumnInfo(name = "parentStation") @SerializedName("parentStation") val parentStation: String?) {

    fun getLocation(): LatLng = LatLng(latitude, longitude)

}