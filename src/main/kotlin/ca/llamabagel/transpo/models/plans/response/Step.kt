/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.plans.response

import com.google.gson.annotations.SerializedName
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import java.util.*

/**
 * A step along a travel plan.
 *
 * @property stepTime The time at which this step occurs
 * @property description A human-readable description of the step
 * @property type The type of step this is, used to parse the data for the data property.
 * @see data
 * @property data Extra data associated with this stop. Depends on the type of step (walk, bus, train, wait, etc.) Will be a child of [StepDetails]
 * @see StepDetails
 * @property path An string-encoded polyline of the geometry of this step.
 */
data class Step(@SerializedName("stepTime") val stepTime: Date,
                @SerializedName("description") val description: String,
                @SerializedName("type") val type: String,
                @SerializedName("data") val data: StepDetails,
                @SerializedName("path") val path: String)

/**
 * Additional details about a step that can be conveyed to the user. There are different types of steps that could occur
 * during a travel plan that each contain very different data, so we use different classes to represent these extra pieces
 * of information. Each child of this class contains the information associated with a different type of step.
 */
sealed class StepDetails {

    /**
     * Details about waiting at a stop. This is used to display information relevant to a stop that the user may be have
     * to wait at including live updates and live GPS times.
     *
     * @property stopId The ID of the stop
     * @property routes A list of possible routes the user could wait for (sometimes more than one route will bring them
     * to the same destination).
     * @property waitDuration The approx. scheduled wait duration in minutes.
     */
    data class StopDetails(@SerializedName("stopId") val stopId: String,
                           @SerializedName("routes") val routes: List<String>,
                           @SerializedName("waitDuration") val waitDuration: Int) : StepDetails()

    /**
     * Details about traveling by bus. This is a step in which the user has boarded a bus (i.e. preceded by a [StopDetails])
     * and can be used to display information such as updates on when to get off, and live GPS updates on when they'll
     * reach their destination.
     *
     * @property routes A list of the the routes the user could be taking
     * @property offStopId The ID of the stop the user should get off at.
     * @property duration Approx. how long this segment of bus travel is scheduled to be in minutes.
     */
    data class BusDetails(@SerializedName("routes") val routes: List<String>,
                          @SerializedName("offStopId") val offStopId: String,
                          @SerializedName("duration") val duration: Int) : StepDetails()

    /**
     * Similar to [BusDetails] but by using a train line.
     *
     * Although there are no overlapping rail lines in Ottawa, [lines] is provided as a list for extensibility and future-proofing.
     *
     * @property lines A list of lines the user could take
     * @property offStopId The stop/station at which the user gets off
     * @property duration Approx. how long this segment of bus travel is scheduled to be in minutes.
     */
    data class TrainDetails(@SerializedName("routes") val lines: List<String>,
                            @SerializedName("offStopId") val offStopId: String,
                            @SerializedName("duration") val duration: Int) : StepDetails()

    /**
     * Details about a walking portion of the travel plan between two points.
     *
     * @property origin The name/description of the starting point of the walk.
     * @property destination The name/description of the end point of the walk.
     * @property duration An estimate of how long it will take to walk this segment.
     */
    data class WalkDetails(@SerializedName("origin") val origin: String,
                           @SerializedName("destination") val destination: String,
                           @SerializedName("duration") val duration: Int) : StepDetails()
}

/**
 * TypeAdapterFactory for serializing and deserializing [StepDetails] objects.
 * @see StepDetails
 */
val stepDetailsTypeFactory: RuntimeTypeAdapterFactory<StepDetails> = RuntimeTypeAdapterFactory
        .of(StepDetails::class.java, "type")
        .registerSubtype(StepDetails.StopDetails::class.java, "stop")
        .registerSubtype(StepDetails.BusDetails::class.java, "bus")
        .registerSubtype(StepDetails.TrainDetails::class.java, "train")
        .registerSubtype(StepDetails.WalkDetails::class.java, "walk")