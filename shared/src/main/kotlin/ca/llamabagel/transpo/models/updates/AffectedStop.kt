/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.updates

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "affected_stops", primaryKeys = ["stopCode", "articleGuid"])
data class AffectedStop(@ColumnInfo(name = "stopCode") val stopCode: String,
                        @ColumnInfo(name = "articleGuid") val articleGuid: String,
                        @ColumnInfo(name = "acknowledged") val acknowledged: Boolean = false)