/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import ca.llamabagel.transpo.utils.VersionSerializer
import kotlinx.serialization.Serializable

@Serializable
data class MetadataRequest(@Serializable(with = VersionSerializer::class) val currentDataVersion: Version,
                           val supportedSchemaVersion: Int,
                           val currentAppCode: Int,
                           val platform: String)