/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.*
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.streams.toList

typealias KeyFunction<R> = (Any) -> R?
typealias CastFunction<T> = (String) -> T?

val Stop.Companion.key: KeyFunction<StopId>
    get() = {
        when (it) {
            is Stop -> it.id
            else -> throw IllegalArgumentException("$it was not a Stop or CsvStop object")
        }
    }

val Route.Companion.key: KeyFunction<RouteId>
    get() = {
        when (it) {
            is CsvRoute -> it.id
            is Route -> it.id
            else -> throw IllegalArgumentException("$it was not a Route or CsvRoute object")
        }
    }

val Agency.Companion.key: KeyFunction<AgencyId>
    get() = {
        when (it) {
            is CsvAgency -> it.id
            is Agency -> it.id
            else -> throw IllegalArgumentException("$it was not an Agency or CsvAgency object")
        }
    }

val Calendar.Companion.key: KeyFunction<CalendarServiceId>
    get() = {
        when (it) {
            is CsvCalendar -> it.serviceId
            is Calendar -> it.serviceId
            else -> throw IllegalArgumentException("$it was not an Calendar or CsvCalendar object")
        }
    }

val CalendarDate.Companion.key: KeyFunction<String>
    get() = {
        when (it) {
            is CsvCalendarDate -> it.serviceId.value + it.date
            is CalendarDate -> it.serviceId.value + it.date
            else -> throw IllegalArgumentException("$it was not an CalendarDate or CsvCalendarDate object")
        }
    }

val StopTime.Companion.key: KeyFunction<String>
    get() = {
        when (it) {
            is CsvStopTime -> it.tripId.value + it.stopId.value
            is StopTime -> it.tripId.value + it.stopId.value
            else -> throw IllegalArgumentException("$it was not an StopTime or CsvStopTime object")
        }
    }

val Trip.Companion.key: KeyFunction<TripId>
    get() = {
        when (it) {
            is CsvTrip -> it.tripId
            is Trip -> it.tripId
            else -> throw IllegalArgumentException("$it was not an StopTime or CsvStopTime object")
        }
    }

/**
 * Inserts the given list of items as rows in a csv file.
 *
 * @param path Full path to the csv file
 * @param objectInitializer Function reference to the [CsvObject] inline class constructor that constructs the CsvObject instance from a [GtfsObject]
 * @param key Function that returns the unique key or id of an object of type [T]. This function should accept parameters of both type [T] and [C].
 * @param items The list of items to be inserted
 *
 * @param T The type of GtfsObject being inserted
 * @param C The type of the inline class used to wrap a List<String?> as a [T]
 * @param R The type of of the key or id of each [T]
 *
 * @return True if all items were inserted successfully, false otherwise.
 */
inline fun <T : GtfsObject, C : CsvObject<T>, R> insertCsvRows(path: Path, duplicateChecker: (R) -> T?, crossinline key: KeyFunction<R>, crossinline objectInitializer: (T) -> C, vararg items: T): Boolean {
    val values = "\n" +
            items.map {
                if (duplicateChecker(key(it)!!) != null) return false
                "${objectInitializer(it).toCsvRow()}\n"
            }.joinToString(separator = "\n")

    Files.write(path, values.toByteArray(), StandardOpenOption.APPEND)

    return true
}

fun <T : GtfsObject, R> insertCsvRows(path: Path, key: KeyFunction<R>, duplicateChecker: (R) -> T?, converter: CsvObjectConverter<T>, vararg items: T): Boolean {
    val lines = items.map {
                if (duplicateChecker(key(it)!!) != null) return false
        converter.getParts(it).toCsv()
            }

    Files.write(path, "\n".toByteArray(), StandardOpenOption.APPEND)
    Files.write(path, lines, StandardOpenOption.APPEND)

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
 *
 * @return True if all items were updated successfully, false otherwise.
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

fun <T : GtfsObject, R> updateCsvRows(path: Path, key: KeyFunction<R>, converter: CsvObjectConverter<T>, vararg items: T): Boolean {
    val mapped = items.associateBy(key, { it }).toMutableMap()

    val updatedLines = Files.lines(path).use { stream ->
        stream.skip(1).map {
            val csv = converter.createObject(it.split(","))

            val csvKey = key(csv)
            // If the object needs to be updated, map this line to the new line's value. Remove this stop from the stops map.
            if (mapped.containsKey(csvKey)) converter.getParts(mapped.getValue(csvKey)).toCsv().also { mapped.remove(csvKey) } else it
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
 *
 * @return True if all items were deleted successfully, false otherwise.
 */
inline fun <T : GtfsObject, C : CsvObject<T>, R> deleteCsvRows(path: Path, crossinline stringInitializer: (String) -> C, crossinline key: KeyFunction<R>,
                                                               vararg objects: T): Boolean {
    val mapped = objects.associateBy(key, { it }).toMutableMap()
    val headers = Files.lines(path).use {stream ->
        stream.findFirst().get()
    }

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
        Files.write(path, headers.toByteArray())
        Files.write(path, updatedLines, StandardOpenOption.APPEND)
        return true
    }

    return false
}

/**
 * Gets an item from a csv file by a specified key. This function will return the first item that matches the given key.
 *
 * @param path Full path to the csv file
 * @param stringInitializer Function reference to the [CsvObject] inline class constructor that constructs the CsvObject instance from a string.
 * @param keyFunction Function that returns the unique key or id of an object of type [T]. This function should accept parameters of both type [T] and [C].
 * @param key The key of the item to find
 *
 * @param T The type of GtfsObject being deleted
 * @param C The type of the inline class used to wrap a List<String?> as a [T]
 * @param R The type of of the key or id of each [T]
 *
 * @return The first item found or null if the item doesn't exist.
 */
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

/**
 * Gets all the items from a csv file that match a specified key.
 *
 * @param path Full path to the csv file
 * @param stringInitializer Function reference to the [CsvObject] inline class constructor that constructs the CsvObject instance from a string.
 * @param keyFunction Function that returns the unique key or id of an object of type [T]. This function should accept parameters of both type [T] and [C].
 * @param key The key of the item to find
 *
 * @param T The type of GtfsObject being deleted
 * @param C The type of the inline class used to wrap a List<String?> as a [T]
 * @param R The type of of the key or id of each [T]
 *
 * @return List of all items that match the key
 */
inline fun <T : GtfsObject, C : CsvObject<T>, R> getItemsByKey(path: Path, crossinline stringInitializer: (String) -> C, crossinline keyFunction: KeyFunction<R>, key: R): List<T> {
    return Files.lines(path).use { stream ->
        stream.skip(1)
                .filter { keyFunction(stringInitializer(it)) == key }
                .map { stringInitializer(it).toObject() }
                .toList()
    }
}

/**
 * Gets all items from a csv file.
 *
 * @param path Full path to the csv file
 * @param stringInitializer Function reference to the [CsvObject] inline class constructor that constructs the CsvObject instance from a string.
 *
 * @param T The type of GtfsObject being deleted
 * @param C The type of the inline class used to wrap a List<String?> as a [T]
 *
 * @return List of all items in the csv file.
 */
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

fun List<String?>.toCsv() = this.joinToString(separator = ",") { it ?: "" }

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CsvHeader(val name: String)

class CsvObjectConverter<T : GtfsObject>(private val clazz: KClass<T>, headers: List<String>) {
    private val parameters: MutableMap<String, KParameter> = mutableMapOf()
    private val properties: MutableMap<String, KProperty1<T, *>> = mutableMapOf()

    init {
        val params = clazz.primaryConstructor?.parameters
        for (header in headers) {
            val parameter = params?.find { param -> param.findAnnotation<CsvHeader>()?.name == header }
            if (parameter != null) {
                parameters[header] = parameter
                val prop = clazz.declaredMemberProperties.find { it.name == parameter.name } ?: throw NullPointerException("Couldn't find matching property")
                properties[header] = prop
            }
        }
    }

    fun createObject(parts: List<String?>): T {
        val params = mutableMapOf<KParameter, Any?>()

        parameters.toList().forEachIndexed { index, pair ->
            params[pair.second] = parts[index]
        }

        return clazz.primaryConstructor!!.callBy(params)
    }

    fun getParts(obj: T): List<String?> {
        val parts = mutableListOf<String?>()

        properties.forEach { _, prop ->
            parts.add(prop.get(obj)?.toString())
        }

        return parts
    }

    private fun castParameter(parameter: KParameter, value: Any?): Any? {
        return null ///parameter.type.jvmErasure
    }
}

inline class CsvRoute(val parts: List<String?>) : CsvObject<Route> {
    constructor(string: String) : this(string.split(","))

    constructor(route: Route) : this(listOf(route.id.value, route.agencyId?.value, route.shortName, route.longName, route.description, route.type.toString(), route.url, route.color, route.textColor, route.sortOrder?.toString()))

    inline val id: RouteId get() = parts[0].asRouteId()!!
    inline val agencyId: AgencyId? get() = parts[1].nullIfBlank().asAgencyId()
    inline val shortName: String get() = parts[2]!!
    inline val longName: String get() = parts[3]!!
    inline val description: String? get() = parts[4].nullIfBlank()
    inline val type: Int get() = parts[5]!!.toInt()
    inline val url: String? get() = parts[6].nullIfBlank()
    inline val color: String? get() = parts[7].nullIfBlank()
    inline val textColor: String? get() = parts[8].nullIfBlank()
    inline val sortOrder: Int? get() = parts[9]?.toIntOrNull()

    override fun toObject() = Route(id, agencyId, shortName, longName, description, type, url, color, textColor, sortOrder)

    override fun toCsvRow() = CsvObject.partsToCsv(parts)
}

inline class CsvAgency(val parts: List<String?>) : CsvObject<Agency> {
    constructor(string: String) : this(string.split(","))

    constructor(agency: Agency) : this(listOf(agency.id.value, agency.name, agency.url, agency.timeZone, agency.language, agency.phone, agency.fareUrl, agency.email))

    inline val id: AgencyId get() = parts[0].asAgencyId()!!
    inline val name: String get() = parts[1]!!
    inline val url: String get() = parts[2]!!
    inline val timeZone: String get() = parts[3]!!
    inline val language: String? get() = parts[4].nullIfBlank()
    inline val phone: String? get() = parts[5].nullIfBlank()
    inline val fareUrl: String? get() = parts[6].nullIfBlank()
    inline val email: String? get() = parts[7].nullIfBlank()

    override fun toObject() = Agency(id, name, url, timeZone, language, phone, fareUrl, email)

    override fun toCsvRow() = CsvObject.partsToCsv(parts)
}

inline class CsvCalendar(val parts: List<String?>) : CsvObject<Calendar> {

    constructor(string: String) : this(string.split(","))

    constructor(calendar: Calendar) : this(listOf(calendar.serviceId.value, calendar.monday.toString(), calendar.tuesday.toString(), calendar.wednesday.toString(), calendar.thursday.toString(), calendar.friday.toString(), calendar.saturday.toString(), calendar.sunday.toString()))

    inline val serviceId: CalendarServiceId get() = parts[0].asCalendarServiceId()!!
    inline val monday: Int get() = parts[1]!!.toInt()
    inline val tuesday: Int get() = parts[2]!!.toInt()
    inline val wednesday: Int get() = parts[3]!!.toInt()
    inline val thursday: Int get() = parts[4]!!.toInt()
    inline val friday: Int get() = parts[5]!!.toInt()
    inline val saturday: Int get() = parts[6]!!.toInt()
    inline val sunday: Int get() = parts[7]!!.toInt()
    inline val startDate: String get() = parts[8]!!
    inline val endDate: String get() = parts[9]!!

    override fun toObject() = Calendar(serviceId, monday, tuesday, wednesday, thursday, friday, saturday, sunday, startDate, endDate)

    override fun toCsvRow() = CsvObject.partsToCsv(parts)
}

inline class CsvCalendarDate(val parts: List<String?>) : CsvObject<CalendarDate> {

    constructor(string: String) : this(string.split(","))

    constructor(calendarDate: CalendarDate) : this(listOf(calendarDate.serviceId.value, calendarDate.date, calendarDate.exceptionType.toString()))

    inline val serviceId: CalendarServiceId get() = parts[0].asCalendarServiceId()!!
    inline val date: String get() = parts[1]!!
    inline val exceptionType: Int get() = parts[2]!!.toInt()

    override fun toObject() = CalendarDate(serviceId, date, exceptionType)

    override fun toCsvRow() = CsvObject.partsToCsv(parts)
}

inline class CsvStopTime(val parts: List<String?>) : CsvObject<StopTime> {

    constructor(string: String) : this(string.split(","))

    constructor(stopTime: StopTime) : this(listOf(stopTime.tripId.value, stopTime.arrivalTime, stopTime.departureTime, stopTime.stopId.value, stopTime.stopSequence.toString(), stopTime.stopHeadsign, stopTime.pickupType?.toString(), stopTime.dropOffType?.toString(), stopTime.shapeDistanceTraveled?.toString(), stopTime.timepoint?.toString()))

    inline val tripId: TripId get() = parts[0].asTripId()!!
    inline val arrivalTime: String get() = parts[1]!!
    inline val departureTime: String get() = parts[2]!!
    inline val stopId: StopId get() = parts[3].asStopId()!!
    inline val stopSequence: Int get() = parts[4]!!.toInt()
    inline val stopHeadsign: String? get() = parts[5].nullIfBlank()
    inline val pickupType: Int? get() = parts[6]?.toIntOrNull()
    inline val dropOffType: Int? get() = parts[7]?.toIntOrNull()
    inline val shapeDistanceTraveled: Double? get() = parts[8]?.toDoubleOrNull()
    inline val timepoint: Int? get() = parts[9]?.toIntOrNull()

    override fun toObject() = StopTime(tripId, arrivalTime, departureTime, stopId, stopSequence, stopHeadsign, pickupType, dropOffType, shapeDistanceTraveled, timepoint)

    override fun toCsvRow() = CsvObject.partsToCsv(parts)

}

inline class CsvTrip(val parts: List<String?>) : CsvObject<Trip> {
    constructor(string: String) : this(string.split(","))

    constructor(trip: Trip) : this(listOf(trip.routeId.value, trip.serviceId.value, trip.tripId.value, trip.headsign, trip.shortName, trip.directionId?.toString(), trip.blockId, trip.shapeId, trip.wheelchairAccessible?.toString(), trip.bikesAllowed?.toString()))

    inline val routeId: RouteId get() = parts[0].asRouteId()!!
    inline val serviceId: CalendarServiceId get() = parts[1].asCalendarServiceId()!!
    inline val tripId: TripId get() = parts[2].asTripId()!!
    inline val headsign: String? get() = parts[3].nullIfBlank()
    inline val shortName: String? get() = parts[4].nullIfBlank()
    inline val directionId: Int? get() = parts[5]?.toIntOrNull()
    inline val blockId: String? get() = parts[6].nullIfBlank()
    inline val shapeId: String? get() = parts[7].nullIfBlank()
    inline val wheelchairAccessible: Int? get() = parts[8]?.toIntOrNull()
    inline val bikesAllowed: Int? get() = parts[9]?.toIntOrNull()

    override fun toObject() = Trip(routeId, serviceId, tripId, headsign, shortName, directionId, blockId, shapeId, wheelchairAccessible, bikesAllowed)

    override fun toCsvRow() = CsvObject.partsToCsv(parts)

}