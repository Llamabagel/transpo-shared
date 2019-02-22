/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao

/**
 * A [Dao] interface for items of type [T] that can be updated.
 * This is typically best suited for data types that can be uniquely identified using a single value.
 *
 * Data types that use composite identifiers should be deleted and re-inserted if an "update" is required and should
 * just use the base [Dao] interface instead.
 */
interface UpdatableDao<T> : Dao<T> {

    /**
     * Updates a [T] in the data source.
     * The identifying property that determines which object is actually updated
     * depends on the implementation.
     *
     * @param t One or more objects to update.
     * @return true if the given items were all updated successfully, false if not
     */
    fun update(vararg t: T): Boolean

}