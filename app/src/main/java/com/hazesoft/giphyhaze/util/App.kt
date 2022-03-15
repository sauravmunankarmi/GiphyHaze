package com.hazesoft.giphyhaze.util

import android.app.Application
import android.content.Context
import com.hazesoft.giphyhaze.db.FavoriteGiphyGifDatabase
import com.hazesoft.giphyhaze.repository.GifRepository

/**
 * Created by Saurav
 * on 3/15/2022
 */
class App: Application() {
    val database by lazy { FavoriteGiphyGifDatabase.getDatabase(this) }
    val repository by lazy { GifRepository(database.favoriteGiphyGifDao()) }


//    companion object {
//        var database: FavoriteGiphyGifDatabase? = null
//        var repository: GifRepository? = null
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        database = FavoriteGiphyGifDatabase.getDatabase(this)
//        repository = GifRepository(database!!.favoriteGiphyGifDao())
//
//    }

}