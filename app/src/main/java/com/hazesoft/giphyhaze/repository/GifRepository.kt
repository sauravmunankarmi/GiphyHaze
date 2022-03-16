package com.hazesoft.giphyhaze.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.hazesoft.giphyhaze.api.ApiInterface
import com.hazesoft.giphyhaze.db.FavoriteGiphyGif
import com.hazesoft.giphyhaze.db.FavoriteGiphyGifDao
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.util.App
import com.hazesoft.giphyhaze.util.Constants
import kotlinx.coroutines.flow.Flow

/**
 * Created by Saurav
 * on 3/15/2022
 */
class GifRepository(private val favoriteGiphyGifDao: FavoriteGiphyGifDao) {

    private val apiInterface = ApiInterface.create()

//    suspend fun getTrendingGifs() = apiInterface.getTrendingGifs(Constants.GIPHY_API_KEY, 20)

    fun getTrendingGifs() = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 40,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            GiphyGifPagingSource(
                apiInterface
            )
        }

    ).liveData

    fun getSearchedGifs(searchString: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 40,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            SearchedGiphyGifPagingSource(
                apiInterface,
                searchString
            )
        }

    ).liveData

//    suspend fun getSearchedGifs(searchString: String) = apiInterface.getSearchedGifs(Constants.GIPHY_API_KEY, searchString, 20)

    val allFavoritesGiphyGif: Flow<List<FavoriteGiphyGif>> = favoriteGiphyGifDao.getAllFavoriteGiphyGif()

    suspend fun allFavoritesGiphyGifList() = favoriteGiphyGifDao.getAllFavoriteGiphyGifList()

    suspend fun addFavoriteGiphyGif(giphyGif: GiphyGif) = favoriteGiphyGifDao.insertFavoriteGiphyGif(
        FavoriteGiphyGif(
            giphyGif.giphyId,
            giphyGif.giphyGifUrl,
        )
    )

    suspend fun removeFavoriteGiphyGif(giphyGif: GiphyGif) = favoriteGiphyGifDao.deleteFavoriteGiphyGifByGiphyId(giphyGif.giphyId)
}