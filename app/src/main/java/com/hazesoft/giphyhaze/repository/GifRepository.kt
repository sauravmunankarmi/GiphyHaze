package com.hazesoft.giphyhaze.repository

import com.hazesoft.giphyhaze.api.ApiInterface
import com.hazesoft.giphyhaze.db.FavoriteGiphyGif
import com.hazesoft.giphyhaze.db.FavoriteGiphyGifDao
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.util.App
import com.hazesoft.giphyhaze.util.Constants

/**
 * Created by Saurav
 * on 3/15/2022
 */
class GifRepository(private val favoriteGiphyGifDao: FavoriteGiphyGifDao) {

    private val apiInterface = ApiInterface.create()

    suspend fun getTrendingGifs() = apiInterface.getTrendingGifs(Constants.GIPHY_API_KEY)

    suspend fun getAllFavoriteGiphyGif() = App.database!!.favoriteGiphyGifDao().getAllFavoriteGiphyGif()

    suspend fun addFavoriteGiphyGif(giphyGif: GiphyGif) = App.database!!.favoriteGiphyGifDao().insertFavoriteGiphyGif(
        FavoriteGiphyGif(
            giphyGif.giphyId,
            giphyGif.giphyGifUrl,
        )
    )

    suspend fun removeFavoriteGiphyGif(giphyGif: GiphyGif) = App.database!!.favoriteGiphyGifDao().deleteFavoriteGiphyGifByGiphyId(giphyGif.giphyId)
}