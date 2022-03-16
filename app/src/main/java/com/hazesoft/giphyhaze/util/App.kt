package com.hazesoft.giphyhaze.util

import android.app.Application
import com.hazesoft.giphyhaze.db.FavoriteGiphyGifDatabase
import com.hazesoft.giphyhaze.repository.GifRepository

/**
 * Created by Saurav
 * on 3/15/2022
 */
class App: Application() {
    private val database by lazy { FavoriteGiphyGifDatabase.getDatabase(this) }
    val repository by lazy { GifRepository(database.favoriteGiphyGifDao()) }
}