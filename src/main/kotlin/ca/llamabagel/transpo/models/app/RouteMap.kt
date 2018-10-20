package ca.llamabagel.transpo.models.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by isaac on 8/27/2017.
 * @property route The shortName of the route whose data is contained
 * @property directionId The direction id of the route whose data is contained
 * @property pathDataString The string form of the json data for the path data
 * @property pathData The proper representation of the data in the pathDataString property
 *
 * TODO: Replace this with a proper shapes.txt implementation
 */
@Entity(tableName = "route_maps", primaryKeys = ["route", "directionId"])
data class RouteMap(@ColumnInfo(name = "route") @SerializedName("route") var route: String,
               @ColumnInfo(name = "directionId") @SerializedName("directionId") var directionId: Int,
               @ColumnInfo(name = "pathData") @SerializedName("pathData") var pathDataString: String)