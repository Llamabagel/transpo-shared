/*
 * Copyright (c) 2018. Llamabagel
 *
 * This file is a part of Route 613.
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