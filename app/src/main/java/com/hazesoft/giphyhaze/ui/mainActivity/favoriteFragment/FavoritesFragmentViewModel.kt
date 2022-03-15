package com.hazesoft.giphyhaze.ui.mainActivity.favoriteFragment

import androidx.lifecycle.*
import com.hazesoft.giphyhaze.db.FavoriteGiphyGif
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import com.hazesoft.giphyhaze.util.App
import kotlinx.coroutines.*

/**
 * Created by Saurav
 * on 3/15/2022
 */
class FavoritesFragmentViewModel(private val gifRepository: GifRepository): ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    val message = MutableLiveData<String>()

    //transforming livedata list of "FavoriteGiphyGif" from db to livedata list of "GiphyGif"
    val favoriteGiphyGifDisplayList: LiveData<List<GiphyGif>> = Transformations.map(gifRepository.allFavoritesGiphyGif.asLiveData()){ it ->
        val tempList = ArrayList<GiphyGif>()
        it.forEach {
            tempList.add(
                GiphyGif(
                it.giphyId,
                it.giphyGifUrl,
                true
                )
            )
        }
        return@map tempList
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        message.value = throwable.message
    }

    fun removeFavoriteGiphyGifFromDb(giphyGif: GiphyGif){
        val job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            gifRepository!!.removeFavoriteGiphyGif(giphyGif)
        }
    }


}