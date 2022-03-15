package com.hazesoft.giphyhaze.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Saurav
 * on 3/15/2022
 */

@Database(entities = arrayOf(FavoriteGiphyGif::class), version = 1, exportSchema = true)
abstract class FavoriteGiphyGifDatabase: RoomDatabase() {
    abstract fun favoriteGiphyGifDao() : FavoriteGiphyGifDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: FavoriteGiphyGifDatabase? = null

        fun getDatabase(context: Context): FavoriteGiphyGifDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteGiphyGifDatabase::class.java,
                    "favorite_giphy_gif_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}