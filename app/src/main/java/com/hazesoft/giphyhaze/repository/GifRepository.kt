package com.hazesoft.giphyhaze.repository

import com.hazesoft.giphyhaze.api.ApiInterface
import com.hazesoft.giphyhaze.util.Constants

/**
 * Created by Saurav
 * on 3/15/2022
 */
class GifRepository {

    private val apiInterface = ApiInterface.create()

    suspend fun getTrendingGifs() = apiInterface.getTrendingGifs(Constants.GIPHY_API_KEY)
}