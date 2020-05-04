/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import kotlinx.serialization.Serializable

@Serializable
data class MetadataRequest(
        val currentDataVersion: String,
        val supportedSchemaVersion: Int,
        val currentAppCode: Int,
        val platform: String
)
