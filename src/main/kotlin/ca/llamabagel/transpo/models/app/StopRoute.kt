package ca.llamabagel.transpo.models.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by isaac on 7/19/2017.
 * @property stop The stop for this stop route
 * @property route The id of the route that passes through the stop. See [StopRoute.getRouteNumber] for just the route number.
 * @property directionId the direction that the route is travelling (either 0 or 1)
 */
@Entity(tableName = "stop_routes", primaryKeys = ["stop", "route", "directionId"])
data class StopRoute(@ColumnInfo(name = "stop") @SerializedName("stop") val stop: String,
                     @ColumnInfo(name = "route") @SerializedName("route") val route: String,
                     @ColumnInfo(name = "directionId") @SerializedName("directionId") val directionId: Int) {

    /**
     * Returns only the route number form the route field (which is the route's id.)
     */
    fun getRouteNumber(): String = route.split("-").first()

}