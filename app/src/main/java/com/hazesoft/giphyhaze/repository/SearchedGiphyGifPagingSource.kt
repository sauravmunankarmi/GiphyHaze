package com.hazesoft.giphyhaze.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hazesoft.giphyhaze.api.ApiInterface
import com.hazesoft.giphyhaze.model.GiphyResponseModel
import com.hazesoft.giphyhaze.util.Constants
import okio.IOException
import retrofit2.HttpException

/**
 * Created by Saurav
 * on 3/16/2022
 */

class SearchedGiphyGifPagingSource(
    private val apiInterface: ApiInterface,
    private val query: String
): PagingSource<Int, GiphyResponseModel.Data>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GiphyResponseModel.Data> {
        val position = params.key ?: Constants.GIPHY_GIF_STARTING_PAGE_OFFSET

        try{
            val response = apiInterface.getSearchedGifs(
                Constants.GIPHY_API_KEY,
                query,
                Constants.GIPHY_GIF_PAGE_LIMIT,
                position
            )
            val giphyGifList = response.body()!!.data
            return LoadResult.Page(
                giphyGifList,
                prevKey = if(position == Constants.GIPHY_GIF_STARTING_PAGE_OFFSET) null else position - Constants.GIPHY_GIF_PAGE_LIMIT,
                nextKey = if(giphyGifList.isEmpty()) null else position + Constants.GIPHY_GIF_PAGE_LIMIT
            )

        }catch (e: IOException){
            return LoadResult.Error(e)
        }catch (e: HttpException){
            return LoadResult.Error(e)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, GiphyResponseModel.Data>): Int? {
        TODO("Not yet implemented")
    }

}