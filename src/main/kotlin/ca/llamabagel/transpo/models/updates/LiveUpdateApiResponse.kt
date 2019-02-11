/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.updates

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Live Update entry model
 * @property title The title of the post
 * @property date The date the update was posted in unix seconds
 * @property category The category this update belongs to
 * @property link The link to the update
 * @property description The actual content of the update. In raw HTML
 * @property guid The GUID for the post
 * @property featuredImageUrl The url to the update's featured image, if there is one.
 * @property isDismissed Whether this update has been dismissed from the home screen
 * @property affectedRoutes A list of routes affected by the LiveUpdateApiResponse
 * @property affectedStops A list of stops affected by this LiveUpdateApiResponse
 */
data class LiveUpdateApiResponse(@SerializedName("title") val title: String,
                                 @SerializedName("publicationDate") val date: Long = 0,
                                 @SerializedName("category") var category: String,
                                 @SerializedName("link") val link: String,
                                 @SerializedName("description") var description: String,
                                 @SerializedName("guid") var guid: String,
                                 @SerializedName("featuredImageUrl") var featuredImageUrl: String? = null) : Serializable {

    @Transient
    var isDismissed: Boolean = false

    @SerializedName("affectedRoutes")
    var affectedRoutes: ArrayList<String> = ArrayList()

    @SerializedName("affectedStops")
    var affectedStops: ArrayList<String> = ArrayList()

    /**
     * Returns this ApiResponse as the actual LiveUpdate object.
     */
    fun getLiveUpdateObject(): LiveUpdate =
            LiveUpdate(title, date, category, link, description, guid, featuredImageUrl)

    /**
     * Returns the affected routes of this article as a list of [AffectedRoute] objects.
     */
    fun getAffectedRouteObjects(): List<AffectedRoute> {
        val result = ArrayList<AffectedRoute>()
        affectedRoutes.mapTo(result) { AffectedRoute(it, guid) }

        return result
    }

    /**
     * Returns the affected stops of this article as a list of [AffectedStop] objects.
     */
    fun getAffectedStopObjects(): List<AffectedStop> {
        val result = ArrayList<AffectedStop>()
        affectedStops.mapTo(result) { AffectedStop(it, guid) }

        return result
    }

}
