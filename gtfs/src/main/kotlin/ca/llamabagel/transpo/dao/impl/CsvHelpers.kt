/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.*

typealias KeyFunction<T, R> = (T) -> R?

val Stop.Companion.key: KeyFunction<Stop, StopId>
    get() = Stop::id

val Route.Companion.key: KeyFunction<Route, RouteId>
    get() = Route::id

val Agency.Companion.key: KeyFunction<Agency, AgencyId>
    get() = Agency::id

val Calendar.Companion.key: KeyFunction<Calendar, CalendarServiceId>
    get() = Calendar::serviceId

val CalendarDate.Companion.key: KeyFunction<CalendarDate, String>
    get() = { it.serviceId.value + it.date }

val StopTime.Companion.key: KeyFunction<StopTime, String>
    get() = { it.tripId.value + it.stopId.value }

val Trip.Companion.key: KeyFunction<Trip, TripId>
    get() = Trip::tripId

/**
 * For parsing csv, if a value is empty it is considered null. This function will return null if the string
 * is null or empty. Otherwise, it just returns itself.
 *
 * @return null if the string is empty or null, or just the string itself otherwise
 */
fun String?.nullIfBlank() = if (isNullOrBlank()) null else this

fun List<String?>.toCsv() = this.joinToString(separator = ",") { it ?: "" }
