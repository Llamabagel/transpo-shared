/*
 * Copyright (c) 2018. Llamabagel
 *
 * This file is a part of Route 613.
 */

package ca.llamabagel.transpo.models.updates

import androidx.room.Embedded
import androidx.room.Relation

class LiveUpdateWithAffected() {

    @Embedded
    lateinit var update: LiveUpdate

    @Relation(parentColumn = "guid", entityColumn = "articleGuid")
    var affectedRoutes: List<AffectedRoute> = emptyList()

    @Relation(parentColumn = "guid", entityColumn = "articleGuid")
    var affectedStops: List<AffectedStop> = emptyList()

}