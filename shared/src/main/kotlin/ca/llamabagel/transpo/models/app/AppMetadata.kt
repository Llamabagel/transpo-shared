/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import ca.llamabagel.transpo.utils.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

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
@Serializable
data class AppMetadata(val dataVersion: String,
                       val dataSchemaVersion: Int,
                       val latestAppVersion: String,
                       @Serializable(with = OffsetDateTimeSerializer::class) val date: OffsetDateTime = OffsetDateTime.now())