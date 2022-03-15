package com.hazesoft.giphyhaze.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Saurav
 * on 3/15/2022
 */

@Entity(tableName = "favorite_giphy_gif_table")
class FavoriteGiphyGif(
    @ColumnInfo(name = "giphy_id")
    val giphyId: String,
    @ColumnInfo(name = "giphy_gif_url")
    val giphyGifUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}