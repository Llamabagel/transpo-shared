package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/transit/gtfs/reference/#tripstxt]
 */
data class Trip(val id: String,
                val serviceId: String,
                val tripId: String,
                val headsign: String?,
                val shortName: String?,
                val directionId: Int?,
                val blockId: String?,
                val shapeId: String?,
                val wheelchairAccessible: Int?,
                val bikesAllowed: Int?)