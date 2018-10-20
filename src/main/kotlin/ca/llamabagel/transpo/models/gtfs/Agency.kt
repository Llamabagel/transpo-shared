package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/app/gtfs/reference/#agencytxt]
 */
data class Agency(val id: String,
                  val name: String,
                  val url: String,
                  val timeZone: String,
                  val language: String?,
                  val phone: String?,
                  val fareUrl: String?,
                  val email: String?)