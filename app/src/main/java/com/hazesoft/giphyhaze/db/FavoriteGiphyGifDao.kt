package com.hazesoft.giphyhaze.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Saurav
 * on 3/15/2022
 */
@Dao
interface FavoriteGiphyGifDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteGiphyGif(favoriteGiphyGif: FavoriteGiphyGif)

    @Query("SELECT * FROM favorite_giphy_gif_table")
    fun getAllFavoriteGiphyGif(): Flow<List<FavoriteGiphyGif>>

    @Query("DELETE FROM favorite_giphy_gif_table WHERE giphy_id = :giphyId")
    suspend fun deleteFavoriteGiphyGifByGiphyId(giphyId: String)

}