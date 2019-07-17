/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao

/**
 * A base Dao interface that contains the definitions of the most basic Dao methods such as [getAll], [insert],
 * and [delete].
 *
 * @param T The type of object being handled by this DAO.
 */
interface Dao<T> {
    /**
     * Gets a [Sequence] of all available [T] from the source.
     *
     * @return A sequence of all [T]
     */
    fun getAll(): Sequence<T>

    /**
     * Insert a [T] into the data source.
     *
     * @param t One or more objects to insert.
     * @return true if the given items were all inserted successfully, false if not
     */
    fun insert(vararg t: T): Boolean

    /**
     * Deletes a [T] in the data source.
     * The identifying property that determines which object is actually deleted
     * depends on the implementation.
     *
     * @param t One or more objects to delete.
     * @return true if the given items were all deleted successfully, false if not
     */
    fun delete(vararg t: T): Boolean
}

fun <T> Dao<T>.listAll() = getAll().toList()