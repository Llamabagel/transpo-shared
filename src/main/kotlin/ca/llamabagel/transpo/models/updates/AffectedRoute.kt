/*
 * Copyright (c) 2018. Llamabagel
 *
 * This file is a part of Route 613.
 */

package ca.llamabagel.transpo.models.updates

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "affected_routes", primaryKeys = ["routeNumber", "articleGuid"])
data class AffectedRoute(@ColumnInfo(name = "routeNumber") val routeNumber: String,
                         @ColumnInfo(name = "articleGuid") val articleGuid: String,
                         @ColumnInfo(name = "acknowledged") val acknowledged: Boolean = false)