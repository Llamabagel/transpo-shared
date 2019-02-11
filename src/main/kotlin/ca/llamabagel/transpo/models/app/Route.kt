/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * A app route.
 *
 * @property id The unique id string for this route.
 * @property shortName A short name for this route (typically just the route number)
 * @property longName The long name of the route (usually combined headings?)
 * @property fromHeading The typical heading of the origin point of this route
 * @property toHeading The typical heading of the destination point of this route
 */
@Entity(tableName = "routes")
data class Route(
        @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: String,
        @ColumnInfo(name = "shortName") @SerializedName("shortName") val shortName: String,
        @ColumnInfo(name = "longName") @SerializedName("longName") val longName: String?,
        @ColumnInfo(name = "fromHeading") @SerializedName("fromHeading") val fromHeading: String?,
        @ColumnInfo(name = "toHeading") @SerializedName("toHeading") val toHeading: String?
) {
    /**
     * An enum representing the different types of routes available
     */
    enum class Type {
        NORMAl,
        CONNEXION,
        PEAK,
        RAPID,
        SCHOOL,
        EVENT,
        FREQUENT,
        RAIL
    }
}