/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/transit/gtfs/reference/#agencytxt]
 */
data class Agency(val id: AgencyId,
                  val name: String,
                  val url: String,
                  val timeZone: String,
                  val language: String?,
                  val phone: String?,
                  val fareUrl: String?,
                  val email: String?) : GtfsObject() {
    companion object
}

inline class AgencyId(val value: String)
fun String?.asAgencyId() = if (this == null) null else AgencyId(this)
