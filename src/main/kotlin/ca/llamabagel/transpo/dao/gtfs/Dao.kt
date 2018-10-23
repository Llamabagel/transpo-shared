package ca.llamabagel.transpo.dao.gtfs

/**
 * A base Dao interface that contains the definitions
 * of the most basic Dao methods such as [getAll], [insert],
 * [update], and [delete].
 *
 * @param T The type of object being handled by this DAO.
 */
interface Dao<T> {

    /**
     * Gets a list of all available [T] from the source.
     *
     * @return A list of all [T]
     */
    fun getAll(): List<T>

    /**
     * Insert a [T] into the data source.
     *
     * @param t The object to insert.
     */
    fun insert(t: T)

    /**
     * Updates a [T] in the data source.
     * The identifying property that determines which object is actually updated
     * depends on the implementation.
     *
     * @param t The object to update.
     */
    fun update(t: T)

    /**
     * Deletes a [T] in the data source.
     * The identifying property that determines which object is actually deleted
     * depends on the implementation.
     *
     * @param t The object to delete.
     */
    fun delete(t: T)

}