/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation

/**
 * A relational data class containing a stop and its [StopRoute]s
 */
class StopWithStopRoutes {
    @Embedded
    lateinit var stop: Stop

    @Relation(parentColumn = "id", entityColumn = "stop", entity = StopRoute::class)
    lateinit var stopRoutes: List<StopRoute>

    @Ignore
    var isCluster: Boolean = false
}