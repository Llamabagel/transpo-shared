package ca.llamabagel.transpo.models.transit

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * A transit route.
 *
 * @property id The unique id string for this route.
 * @property shortName A short name for this route (typically just the route number)
 * @property longName The long name of the route (usually combined headings?)
 * @property fromHeading The typical heading of the origin point of this route
 * @property toHeading The typical heading of the destination point of this route
 */
data class Route(
        @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: String,
        @ColumnInfo(name = "shortName") @SerializedName("shortName") val shortName: String,
        @ColumnInfo(name = "longName") @SerializedName("longName") val longName: String?,
        @ColumnInfo(name = "fromHeading") @SerializedName("fromHeading") val fromHeading: String?,
        @ColumnInfo(name = "toHeading") @SerializedName("toHeading") val toHeading: String?
)