/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.ShapeDao
import ca.llamabagel.transpo.models.gtfs.*
import java.nio.file.Path

/**
 * Implementation of [GtfsDirectory] for processing GTFS data supplied by OC Transpo.
 */
class OcTranspoGtfsDirectory(gtfsPath: Path) : GtfsDirectory(gtfsPath) {

    override val stopsTable = csvTable<Stop> {
        path = this@OcTranspoGtfsDirectory.path.resolve("stops.txt")
        headers = listOf("stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon",
                "zone_id", "stop_url", "location_type")

        objectInitializer {
            Stop(it[0].asStopId()!!, it[1].nullIfBlank(), it[2]!!, it[3].nullIfBlank(), it[4]?.toDoubleOrNull()
                    ?: Double.NaN, it[5]?.toDoubleOrNull()
                    ?: Double.NaN, it[6]?.toIntOrNull(), it[7].nullIfBlank(), it[8]?.toIntOrNull(), null, null, null)
        }

        partsInitializer {
            listOf(it.id.value, it.code, it.name, it.description, it.latitude.toString(), it.longitude.toString(),
                    it.zoneId?.toString(), it.stopUrl, it.locationType?.toString())
        }
    }

    override val routesTable = csvTable<Route> {
        path = this@OcTranspoGtfsDirectory.path.resolve("routes.txt")
        headers = listOf("route_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_url", "route_color", "route_text_color")

        objectInitializer {
            Route(it[0].asRouteId()!!, null, it[1]!!, it[2]!!, it[3].nullIfBlank(), it[4]!!.toInt(), it[5].nullIfBlank(), it[6].nullIfBlank(), it[7].nullIfBlank(), null)
        }

        partsInitializer {
            listOf(it.id.value, it.shortName, it.longName, it.description, it.type.toString(), it.url)
        }
    }

    override val agencyTable = csvTable<Agency> {
        path = this@OcTranspoGtfsDirectory.path.resolve("agency.txt")
        headers = listOf("agency_name", "agency_url", "agency_timezone", "agency_lang", "agency_phone")

        objectInitializer {
            Agency("OC".asAgencyId()!!, it[0]!!, it[1]!!, it[2]!!, it[3].nullIfBlank(), it[4].nullIfBlank(), null, null)
        }

        partsInitializer {
            listOf(it.name, it.url, it.timeZone, it.language, it.phone)
        }
    }

    override val stopTimesTable = csvTable<StopTime> {
        path = this@OcTranspoGtfsDirectory.path.resolve("stop_times.txt")
        headers = listOf("trip_id", "arrival_time", "departure_time", "stop_id", "stop_sequence", "pickup_type", "drop_off_type")

        objectInitializer {
            StopTime(it[0].asTripId()!!, it[1]!!, it[2]!!, it[3].asStopId()!!, it[4]!!.toInt(), null, it[5]?.toIntOrNull(), it[6]?.toIntOrNull(), null, null)
        }

        partsInitializer {
            listOf(it.tripId.value, it.arrivalTime, it.departureTime, it.stopId.value, it.stopSequence.toString(), it.pickupType?.toString(), it.dropOffType?.toString())
        }
    }

    override val tripsTable = csvTable<Trip> {
        path = this@OcTranspoGtfsDirectory.path.resolve("trips.txt")
        headers = listOf("route_id", "service_id", "trip_id", "trip_headsign", "direction_id", "block_id", "shape_id")

        objectInitializer {
            Trip(it[0].asRouteId()!!, it[1].asCalendarServiceId()!!, it[2].asTripId()!!, it[3].nullIfBlank(), null, it[4]?.toIntOrNull(), it[5].nullIfBlank(), it[6]?.asShapeId(), null, null)
        }

        partsInitializer {
            listOf(it.routeId.value, it.serviceId.value, it.tripId.value, it.headsign, it.directionId?.toString(), it.blockId, it.shapeId?.value)
        }
    }

    override val shapesTable: CsvTable<Shape> = csvTable<Shape> {
        path = this@OcTranspoGtfsDirectory.path.resolve("shapes.txt")
        headers = listOf("shape_id", "shape_pt_lat", "shape_pt_lon", "shape_pt_sequence")

        objectInitializer {
            Shape(it[0].asShapeId()!!, it[1]!!.toDouble(), it[2]!!.toDouble(), it[3]!!.toInt(), null)
        }

        partsInitializer {
            listOf(it.id.value, it.latitude.toString(), it.longitude.toString(), it.sequence.toString())
        }
    }

    override val shapes: ShapeDao = object : ShapeDao {
        override fun getById(id: ShapeId): Sequence<Shape> = shapesTable.getItemsByKey(Shape.key, id)

        override fun getAll(): Sequence<Shape> = shapesTable.getAllItems()

        override fun insert(vararg t: Shape): Boolean {
            return shapesTable.insertCsvRows({ "${it.id.value}//${it.sequence}" }, {
                shapesTable.getItemByKey({ "${it.id.value}//${it.sequence}" }, it)
            }, *t)
        }

        override fun delete(vararg t: Shape): Boolean {
            return shapesTable.deleteCsvRows({ "${it.id.value}//${it.sequence}" }, *t)
        }
    }
}