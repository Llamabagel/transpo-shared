/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.transit

import kotlinx.serialization.Serializable

@Serializable
data class StopTime(val id: String,
                    val arrivalTime: String,
                    val departureTime: String,
                    val stopId: String,
                    val stopSequence: Int,
                    val pickupType: Int,
                    val dropOffType: Int)


fun makeStopTime(time: String): StopTime = StopTime("", time, "", "", 0, 0, 0)

val stopTimes = listOf(makeStopTime("01:00"), makeStopTime("02:00"),
        makeStopTime("03:00"), makeStopTime("04:00"), makeStopTime("05:00"),
        makeStopTime("06:00"), makeStopTime("07:00"), makeStopTime("08:00"),
        makeStopTime("09:00"), makeStopTime("10:00"), makeStopTime("11:00"),
        makeStopTime("12:00"), makeStopTime("13:00"), makeStopTime("14:00"),
        makeStopTime("15:00"), makeStopTime("16:00"), makeStopTime("17:00"),
        makeStopTime("18:00"), makeStopTime("19:00"), makeStopTime("20:00"),
        makeStopTime("21:00"), makeStopTime("22:00"), makeStopTime("23:00"))