/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.updates

import ca.llamabagel.transpo.utils.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Live Update entry model
 * @property title The title of the post
 * @property date The date the update was posted in unix seconds
 * @property category The category this update belongs to
 * @property link The link to the update
 * @property description The actual content of the update. In raw HTML
 * @property guid The GUID for the post
 * @property featuredImageUrl The url to the update's featured image, if there is one.
 * @property affectedRoutes A list of routes by their number affected by this update
 * @property affectedStops A list of stops affected by this update by their stop codes
 */
@Serializable
data class LiveUpdate(val title: String,
                      @Serializable(with = DateSerializer::class) val date: Date,
                      val category: String,
                      val link: String,
                      val description: String,
                      val guid: String,
                      val affectedRoutes: List<String> = emptyList(),
                      val affectedStops: List<String> = emptyList(),
                      val featuredImageUrl: String? = null)
