/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.updates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "updates")
data class LiveUpdate(@ColumnInfo(name = "title") val title: String,
                      @ColumnInfo(name = "publicationDate") val date: Long = 0,
                      @ColumnInfo(name = "category") var category: String,
                      @ColumnInfo(name = "link") val link: String,
                      @ColumnInfo(name = "description") var description: String,
                      @PrimaryKey @ColumnInfo(name = "guid") var guid: String,
                      @ColumnInfo(name = "featuredImageUrl") var featuredImageUrl: String? = null) {

    @ColumnInfo(name = "dismissed")
    var dismissed: Boolean = false

}