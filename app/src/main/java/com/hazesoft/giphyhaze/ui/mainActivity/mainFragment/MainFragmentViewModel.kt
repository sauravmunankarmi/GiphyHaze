package com.hazesoft.giphyhaze.ui.mainActivity.mainFragment

import androidx.lifecycle.*
import com.hazesoft.giphyhaze.db.FavoriteGiphyGif
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import com.hazesoft.giphyhaze.util.App
import kotlinx.coroutines.*
import kotlin.random.Random

/**
 * Created by Saurav
 * on 3/15/2022
 */
class MainFragmentViewModel(private val gifRepository: GifRepository): ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    val message = MutableLiveData<String>()

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("error: ${throwable.message}")
        message.value = throwable.message
    }



    private val giphyGifApiList = MutableLiveData<ArrayList<GiphyGif>>(ArrayList())
    var giphyGifDisplayList :LiveData<ArrayList<GiphyGif>> = Transformations.map(gifRepository.allFavoritesGiphyGif.asLiveData()){ favGiphyDbList ->
        val tempList = giphyGifApiList.value
        favGiphyDbList.forEach { favGiphy ->
            favGiphy?.let {
                tempList?.forEach {
                    it.isFavorite = it.giphyId == favGiphy.giphyId
                }
            }
        }

        giphyGifApiList.value = tempList!!
        return@map tempList
    }


    fun getTrendingGif() {
        giphyGifDisplayList.value?.clear()
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = gifRepository!!.getTrendingGifs()
            withContext(Dispatchers.Main) {
//                val favoriteGiphyGifDbList: List<FavoriteGiphyGif>? =
//                    gifRepository.allFavoritesGiphyGif.asLiveData().value
                val tempList = ArrayList<GiphyGif>()
                response.body()?.data?.forEach {
                    println("counter")
                    tempList.add(
                        GiphyGif(
                            it.id,
                            it.images.downsized.url?:"",
                            false
                        )
                    )
                }

                giphyGifApiList.value = tempList
                giphyGifDisplayList = giphyGifApiList


            }
//            withContext(Dispatchers.IO){
//
//            }
        }
    }

    fun favToggle(giphyGif: GiphyGif){

        if(giphyGif.isFavorite){
            //it was favorite now remove
            println("it was favorite now remove because: giphyGif.isFavorite ${giphyGif.isFavorite}")
            removeFavoriteGiphyGifFromDb(giphyGif)
        }else{
            //it was not favorite now it is fav
            println("it was not favorite now it is fav because: giphyGif.isFavorite ${giphyGif.isFavorite}")
            addFavoriteGiphyGifInDb(giphyGif)
        }

    }

    private fun removeFavoriteGiphyGifFromDb(giphyGif: GiphyGif){
        val job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            gifRepository!!.removeFavoriteGiphyGif(giphyGif)
        }
    }

    private fun addFavoriteGiphyGifInDb(giphyGif: GiphyGif){
        val job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            gifRepository!!.addFavoriteGiphyGif(giphyGif)
        }
    }
}