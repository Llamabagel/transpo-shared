package ca.llamabagel.transpo.models

/**
 * Encapsulated WGS 84 Latitude and Longitude point.
 *
 * @property latitude WGS 84 latitude (between -180 and 180)
 * @property longitude WGS 84 longitude (between -180 and 180)
 */
data class LatLng(var latitude: Double, var longitude: Double)