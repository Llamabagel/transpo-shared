package ca.llamabagel.transpo.models.gtfs

data class Stop(val id: String,
                val code: String?,
                val name: String,
                val description: String?,
                val latitude: Double,
                val longitude: Double,
                val zoneId: Int?,
                val stopUrl: String?,
                val locationType: Int?,
                val parentStation: String?,
                val timeZone: String?,
                val wheelchairBoarding: Int?) {
}