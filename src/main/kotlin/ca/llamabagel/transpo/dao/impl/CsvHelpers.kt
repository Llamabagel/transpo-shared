/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.GtfsObject
import ca.llamabagel.transpo.models.gtfs.Stop
import ca.llamabagel.transpo.models.gtfs.StopId
import ca.llamabagel.transpo.models.gtfs.asStopId
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.streams.toList

typealias KeyFunction<R> = (Any) -> R?

val stopKey: KeyFunction<StopId> = {
    when (it) {
        is CsvStop -> it.id
        is Stop -> it.id
        else -> throw IllegalArgumentException("$it was not a Stop or CsvStop object")
    }
}

inline fun <T : GtfsObject, C : CsvObject<T>, R> insertCsvRows(path: Path, duplicateChecker: (R) -> T?, crossinline key: KeyFunction<R>, crossinline objectInitializer: (T) -> C, vararg items: T): Boolean {
    val values = "\n" +
            items.map {
                if (duplicateChecker(key(it)!!) != null) return false
                "${objectInitializer(it).toCsvRow()}\n"
            }.joinToString(separator = "\n")

    Files.write(path, values.toByteArray(), StandardOpenOption.APPEND)

    return true
}

/**
 * Updates the rows of a list of objects in a csv file. Objects are updated based on a specified key.
 *
 * @param path Full path to the csv file
 * @param stringInitializer Function reference to the [CsvObject] inline class constructor that constructs the CsvObject instance from a string.
 * @param objectInitializer Function reference to the CsvObject inline class constructor that constructs the CsvObject instance from a [GtfsObject]
 * @param key Function that returns the unique key or id of an object of type [T]. This function should accept parameters of both type [T] and [C].
 * @param objects The list of objects to be updated
 *
 * @param T The type of GtfsObject being updated
 * @param C The type of the inline class used to wrap a List<String?> as a [T]
 * @param R The type of of the key or id of each [T]
 */
inline fun <T : GtfsObject, C : CsvObject<T>, R> updateCsvRows(path: Path, crossinline stringInitializer: (String) -> C, crossinline objectInitializer: (T) -> C,
                                                                  crossinline key: KeyFunction<R>, vararg objects: T): Boolean {
    val mapped = objects.associateBy(key, { it }).toMutableMap()

    val updatedLines = Files.lines(path).use { stream ->
        stream.map {
            val csv = stringInitializer(it)

            val csvKey = key(csv)
            // If the object needs to be updated, map this line to the new line's value. Remove this stop from the stops map.
            if (mapped.containsKey(csvKey)) objectInitializer(mapped.getValue(csvKey)).toCsvRow().also { mapped.remove(csvKey) } else it
        }.toList()
    }

    // Make sure we've updated all lines, then write everything to the file
    if (mapped.isEmpty()) {
        Files.write(path, updatedLines)
        return true
    }

    return false
}

/**
 * Deletes rows of a list of objects in a csv file. Objects are deleted based on a specified key.
 *
 * @param path Full path to the csv file
 * @param stringInitializer Function reference to the [CsvObject] inline class constructor that constructs the CsvObject instance from a string.
 * @param key Function that returns the unique key or id of an object of type [T]. This function should accept parameters of both type [T] and [C].
 * @param objects The list of objects to be deleted
 *
 * @param T The type of GtfsObject being deleted
 * @param C The type of the inline class used to wrap a List<String?> as a [T]
 * @param R The type of of the key or id of each [T]
 */
inline fun <T : GtfsObject, C : CsvObject<T>, R> deleteCsvRows(path: Path, crossinline stringInitializer: (String) -> C, crossinline key: KeyFunction<R>,
                                                                  vararg objects: T): Boolean {
    val mapped = objects.associateBy(key, { it }).toMutableMap()

    val updatedLines = Files.lines(path).use { stream ->
        stream.map {
            val csv = stringInitializer(it)

            val csvKey = key(csv)
            // If the object needs to be deleted, map this line to a blank value. Remove this stop from the stops map.
            if (mapped.containsKey(key(csv))) "".also { mapped.remove(csvKey) } else it
        }.toList()
    }

    // Make sure we've deleted all lines, then write everything to the file
    if (mapped.isEmpty()) {
        Files.write(path, updatedLines)
        return true
    }

    return false
}

inline fun <T : GtfsObject, C : CsvObject<T>, R> getItemByKey(path: Path, crossinline stringInitializer: (String) -> C, crossinline keyFunction: KeyFunction<R>, key: R): T? {
    return Files.lines(path).use { stream ->
        var item: T? = null

        stream.skip(1).forEach {
            val csv = stringInitializer(it)
            if (keyFunction(csv) == key) {
                item = csv.toObject()
                return@forEach
            }
        }

        return@use item
    }
}

inline fun <T : GtfsObject, C : CsvObject<T>, R> getItemsByKey(path: Path, crossinline stringInitializer: (String) -> C, crossinline keyFunction: KeyFunction<R>, key: R): List<T> {
    return Files.lines(path).use { stream ->
        stream.skip(1)
                .filter { keyFunction(stringInitializer(it)) == key }
                .map { stringInitializer(it).toObject() }
                .toList()
    }
}

inline fun <T : GtfsObject, C : CsvObject<T>> getAllItems(path: Path, crossinline stringInitializer: (String) -> C): List<T> {
    return Files.lines(path).use { stream ->
        stream.skip(1)
                .map { stringInitializer(it).toObject() }
                .toList()
    }
}

/**
 * For parsing csv, if a value is empty it is considered null. This function will return null if the string
 * is null or empty. Otherwise, it just returns itself.
 *
 * @return null if the string is empty or null, or just the string itself otherwise
 */
fun String?.nullIfBlank() = if (isNullOrBlank()) null else this

/**
 * Interface for all inline classes representing some Gtfs csv values.
 *
 * @param T The [GtfsObject] type.
 */
interface CsvObject<T : GtfsObject> {

    /**
     * Converts the csv parts to a [T].
     */
    fun toObject(): T

    /**
     * Converts the csv parts to a row of csv.
     */
    fun toCsvRow(): String

    companion object {
        @JvmStatic
        fun partsToCsv(parts: List<String?>): String = parts.joinToString(separator = ",") { it ?: "" }
    }
}

inline class CsvStop(val parts: List<String?>) : CsvObject<Stop> {

    constructor(string: String) : this(string.split(","))

    constructor(stop: Stop)
            : this(listOf(stop.id.value, stop.code, stop.name, stop.description, stop.latitude.toString(),
            stop.longitude.toString(), stop.zoneId?.toString(), stop.stopUrl, stop.locationType?.toString(),
            stop.parentStation, stop.timeZone, stop.wheelchairBoarding?.toString()))

    // Property accessors for a stop object. Gets the corresponding "part" from the csv.
    inline val id: StopId get() = parts[0].asStopId()!!
    inline val code: String? get() = parts[1].nullIfBlank()
    inline val name: String get() = parts[2]!!
    inline val description: String? get() = parts[3].nullIfBlank()
    inline val latitude: Double get() = parts[4]?.toDoubleOrNull() ?: Double.NaN
    inline val longitude: Double get() = parts[5]?.toDoubleOrNull() ?: Double.NaN
    inline val zoneId: Int? get() = parts[6]?.toIntOrNull()
    inline val stopUrl: String? get() = parts[7].nullIfBlank()
    inline val locationType: Int? get() = parts[8]?.toIntOrNull()
    inline val parentStation: String? get() = parts[9].nullIfBlank()
    inline val timeZone: String? get() = parts[10].nullIfBlank()
    inline val wheelchairBoarding: Int? get() = parts[11]?.toIntOrNull()

    override fun toObject() = Stop(id, code, name, description, latitude, longitude,
            zoneId, stopUrl, locationType, parentStation, timeZone, wheelchairBoarding)

    override fun toCsvRow(): String = CsvObject.partsToCsv(parts)
}