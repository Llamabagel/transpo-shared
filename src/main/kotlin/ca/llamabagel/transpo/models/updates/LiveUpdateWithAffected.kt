/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
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