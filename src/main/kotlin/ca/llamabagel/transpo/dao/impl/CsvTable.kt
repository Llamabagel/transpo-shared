/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.GtfsObject
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.streams.toList

class CsvTable<T : GtfsObject>(private val path: Path,
                               headers: List<String>,
                               private val objectInitializer: ((List<String?>) -> T),
                               private val partsInitializer: ((T) -> List<String?>)) {

    init {
        if (Files.notExists(path)) {
            Files.createFile(path)
        }

        if (Files.lines(path).use { it.findFirst().get() } != headers.toCsv()) {
            Files.write(path, listOf(headers.toCsv()))
        }
    }

    fun <R> insertCsvRows(key: KeyFunction<T, R>, duplicateChecker: (R) -> T?, vararg items: T): Boolean {
        val lines = items.map {
            if (duplicateChecker(key(it)!!) != null) return false
            partsInitializer(it).toCsv()
        }

        Files.write(path, "\n".toByteArray(), StandardOpenOption.APPEND)
        Files.write(path, lines, StandardOpenOption.APPEND)

        return true
    }

    fun <R> updateCsvRows(key: KeyFunction<T, R>, vararg items: T): Boolean {
        val mapped = items.associateBy(key, { it }).toMutableMap()

        val updatedLines = Files.lines(path).use { stream ->
            stream.skip(1).map {
                val csv = objectInitializer(it.split(","))

                val csvKey = key(csv)
                // If the object needs to be updated, map this line to the new line's value. Remove this stop from the stops map.
                if (mapped.containsKey(csvKey)) partsInitializer(mapped.getValue(csvKey)).toCsv().also { mapped.remove(csvKey) } else it
            }.toList()
        }

        // Make sure we've updated all lines, then write everything to the file
        if (mapped.isEmpty()) {
            Files.write(path, updatedLines)
            return true
        }

        return false
    }

    fun <R> deleteCsvRows(key: KeyFunction<T, R>, vararg objects: T): Boolean {
        val mapped = objects.associateBy(key, { it }).toMutableMap()
        val headers = Files.lines(path).use { stream ->
            stream.findFirst().get()
        }

        val updatedLines = Files.lines(path).use { stream ->
            stream.skip(1)
                    .map {
                        val csv = objectInitializer(it.split(","))

                        val csvKey = key(csv)
                        // If the object needs to be deleted, map this line to a blank value. Remove this stop from the stops map.
                        if (mapped.containsKey(key(csv))) null.also { mapped.remove(csvKey) } else it
                    }
                    .filter { it != null }
                    .toList()
        }

        // Make sure we've deleted all lines, then write everything to the file
        if (mapped.isEmpty()) {
            Files.write(path, headers.toByteArray())
            Files.write(path, updatedLines, StandardOpenOption.APPEND)
            return true
        }

        return false
    }

    fun <R> getItemByKey(keyFunction: KeyFunction<T, R>, key: R): T? {
        return Files.lines(path).use { stream ->
            var item: T? = null

            stream.skip(1).forEach {
                val csv = objectInitializer(it.split(","))
                if (keyFunction(csv) == key) {
                    item = csv
                    return@forEach
                }
            }

            return@use item
        }
    }

    fun <R> getItemsByKey(keyFunction: KeyFunction<T, R>, key: R): List<T> {
        return Files.lines(path).use { stream ->
            stream.skip(1)
                    .filter { keyFunction(objectInitializer(it.split(","))) == key }
                    .map { objectInitializer(it.split(",")) }
                    .toList()
        }
    }

    fun getAllItems(): List<T> {
        return Files.lines(path).use { stream ->
            stream.skip(1)
                    .map { objectInitializer(it.split(",")) }
                    .toList()
        }
    }
}

@DslMarker
annotation class CsvTableDsl

@CsvTableDsl
class CsvTableBuilder<T : GtfsObject> {
    var path: Path? = null
    var headers: List<String> = emptyList()
    private var objectInitializer: ((List<String?>) -> T)? = null
    private var partsInitializer: ((T) -> List<String?>)? = null

    fun build(): CsvTable<T> {
        if (path == null) {
            throw IllegalStateException("A path must be specified for this file.")
        }

        if (objectInitializer == null) {
            throw IllegalStateException("objectInitializer must be initialized. are you missing an objectInitializer { } block?")
        }

        if (partsInitializer == null) {
            throw IllegalStateException("partsInitializer must be initialized. are you missing an partsInitializer { } block?")
        }

        return CsvTable(path!!, headers, objectInitializer!!, partsInitializer!!)
    }

    fun objectInitializer(block: (List<String?>) -> T) {
        objectInitializer = block
    }

    fun partsInitializer(block: (T) -> List<String?>) {
        partsInitializer = block
    }
}

fun <T : GtfsObject> csvTable(block: CsvTableBuilder<T>.() -> Unit): CsvTable<T> = CsvTableBuilder<T>().apply(block).build()

