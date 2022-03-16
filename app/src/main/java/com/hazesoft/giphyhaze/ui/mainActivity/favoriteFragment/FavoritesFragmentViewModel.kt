package com.hazesoft.giphyhaze.ui.mainActivity.favoriteFragment

import androidx.lifecycle.*
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by Saurav
 * on 3/15/2022
 */
class FavoritesFragmentViewModel(private val gifRepository: GifRepository): ViewModel() {

    val message = MutableLiveData<String>()

    //transforming livedata list of "FavoriteGiphyGif" (LiveData<List<FavoriteGiphyGif>>) from db to livedata list of "GiphyGif" (LiveData<List<GiphyGif>>) for recycler view adapter
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
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            gifRepository.removeFavoriteGiphyGif(giphyGif)
        }
    }

}