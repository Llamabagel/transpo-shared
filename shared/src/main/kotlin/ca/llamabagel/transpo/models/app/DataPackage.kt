/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.app

import ca.llamabagel.transpo.models.transit.Route
import ca.llamabagel.transpo.models.transit.RouteShape
import ca.llamabagel.transpo.models.transit.Stop
import ca.llamabagel.transpo.models.transit.StopRoute
import ca.llamabagel.transpo.utils.DateSerializer
import ca.llamabagel.transpo.utils.VersionSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * The full package of transit data that is sent by the server when the client downloads a data set.
 *
 * @property dataVersion A string indicating the version of data contained in this package
 * @see Version
 * @property schemaVersion A string indicating what version of the schema this data is stored in
 * @property date The date at which this package was generated
 * @property data The actual data in the package
 * @see Data
 */
@Serializable
data class DataPackage(@Serializable(with = VersionSerializer::class) val dataVersion: Version,
                       val schemaVersion: Int,
                       @Serializable(with = DateSerializer::class) val date: Date,
                       val data: Data)

/**
 * Data contained in a data package
 *
 * @property stops List of all stop data.
 * @see Stop
 *
 * @property routes List of all route data
 * @see Route
 *
 * @property stopRoutes List of all routes and the stops they serve
 * @see StopRoute
 *
 * @property shapes List of all route shape data
 * @see RouteShape
 */
@Serializable
data class Data(val stops: List<Stop>,
                val routes: List<Route>,
                val stopRoutes: List<StopRoute>,
                val shapes: List<RouteShape>)

/**
 * Inline wrapper around a version string that implements a custom comparable interface to allow for comparisons
 * of different version strings.
 */
inline class Version(val value: String) : Comparable<Version> {
    override fun compareTo(other: Version): Int {
        val thisParts = value.split("-")
        val otherParts = other.value.split("-")

        // Compare major version numbers
        val thisMajor = thisParts[0].toInt()
        val otherMajor = otherParts[0].toInt()
        val majorDiff = thisMajor - otherMajor

        if (majorDiff != 0) {
            return majorDiff
        }

        // Compare revision numbers if major versions were the same
        val thisRevision = if (thisParts.size > 1) thisParts[1].toInt() else 0
        val otherRevision = if (otherParts.size > 1) otherParts[1].toInt() else 0
        return thisRevision - otherRevision
    }

    override fun toString(): String = value

}