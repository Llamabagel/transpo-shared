package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/app/gtfs/reference/#calendartxt]
 */
data class Calendar(val serviceId: String,
                    val monday: Int,
                    val tuesday: Int,
                    val wednesday: Int,
                    val thursday: Int,
                    val friday: Int,
                    val saturday: Int,
                    val sunday: Int,
                    val startDate: String,
                    val endDate: String)