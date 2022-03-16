package com.hazesoft.giphyhaze.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Saurav
 * on 3/15/2022
 */

@Database(entities = arrayOf(FavoriteGiphyGif::class), version = 2, exportSchema = true)
abstract class FavoriteGiphyGifDatabase: RoomDatabase() {
    abstract fun favoriteGiphyGifDao() : FavoriteGiphyGifDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteGiphyGifDatabase? = null

        fun getDatabase(context: Context): FavoriteGiphyGifDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteGiphyGifDatabase::class.java,
                    "favorite_giphy_gif_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}