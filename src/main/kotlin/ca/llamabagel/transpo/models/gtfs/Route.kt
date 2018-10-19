package ca.llamabagel.transpo.models.gtfs

data class Route(val id: String,
                 val agencyId: String?,
                 val shortName: String,
                 val longName: String,
                 val description: String?,
                 val type: Int,
                 val url: String?,
                 val color: String?,
                 val textColor: String?,
                 val sortOrder: Int?)