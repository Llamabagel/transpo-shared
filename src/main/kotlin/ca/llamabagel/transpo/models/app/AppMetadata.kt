/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Data class representing metadata about the 'app' as a whole. Specifically, this is the class used by the server
 * to indicate to clients the state of various configurable properties about the data being served. Currently it only
 * reports what the latest data version that is available for download as well as what the latest version of the app on the
 * requesting platform is.
 *
 * @property dataVersion The latest version of data available for the given schema version.
 * @see dataSchemaVersion
 * @see Version
 *
 * @property dataSchemaVersion The schema version of the latest downloadable data version
 * @see dataVersion
 *
 * @property latestAppVersion The latest version of the app available on the requesting client's platform.
 * @property date The date at which this data object was generated.
 */
data class AppMetadata(@SerializedName("dataVersion") val dataVersion: Version,
                       @SerializedName("dataSchemaVersion") val dataSchemaVersion: Int,
                       @SerializedName("latestAppVersion") val latestAppVersion: String,
                       @SerializedName("date") val date: Date = Date())