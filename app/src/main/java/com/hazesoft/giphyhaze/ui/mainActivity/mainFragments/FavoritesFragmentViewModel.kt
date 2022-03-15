package com.hazesoft.giphyhaze.ui.mainActivity.mainFragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import com.hazesoft.giphyhaze.util.App
import kotlinx.coroutines.*

/**
 * Created by Saurav
 * on 3/15/2022
 */
class FavoritesFragmentViewModel(): ViewModel() {

    private val gifRepository = App.repository

    val isLoading = MutableLiveData<Boolean>(true)
    val message = MutableLiveData<String>()

    val favoriteGiphyGifList = MutableLiveData<ArrayList<GiphyGif>>(ArrayList())

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        message.value = throwable.message
    }

    fun getAllFavoriteGiphyGif(){
        isLoading.postValue(true)

        val job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val favoriteGifDbList = gifRepository!!.getAllFavoriteGiphyGif()
            Dispatchers.Main{
                val tempList = ArrayList<GiphyGif>()
                favoriteGifDbList.forEach {
                    tempList.add(
                        GiphyGif(
                            it.giphyId,
                            it.giphyGifUrl,
                            true
                        )
                    )
                }

                favoriteGiphyGifList.value = tempList
            }
        }
    }
}