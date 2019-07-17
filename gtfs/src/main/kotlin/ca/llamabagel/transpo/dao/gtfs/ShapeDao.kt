/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.Shape
import ca.llamabagel.transpo.models.gtfs.ShapeId

interface ShapeDao : Dao<Shape> {
    fun getById(id: ShapeId) : Sequence<Shape>
}

fun ShapeDao.listById(id: ShapeId) = getById(id).toList()