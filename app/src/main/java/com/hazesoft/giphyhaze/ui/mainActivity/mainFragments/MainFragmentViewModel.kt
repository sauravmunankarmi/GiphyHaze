package com.hazesoft.giphyhaze.ui.mainActivity.mainFragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import com.hazesoft.giphyhaze.util.App
import kotlinx.coroutines.*
import kotlin.random.Random

/**
 * Created by Saurav
 * on 3/15/2022
 */
class MainFragmentViewModel: ViewModel() {

    private val gifRepository = App.repository

    val isLoading = MutableLiveData<Boolean>(true)
    val message = MutableLiveData<String>()

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("error: ${throwable.message}")
        message.value = throwable.message
    }


    val trendingList = MutableLiveData<ArrayList<GiphyGif>>(ArrayList())


    fun getTrendingGif(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = gifRepository!!.getTrendingGifs()
            withContext(Dispatchers.Main){
                val tempList = ArrayList<GiphyGif>()
                response.body()?.data?.forEach {
                    tempList.add(
                        GiphyGif(
                            it.id,
                            it.images.downsized.url,
                            Random.nextBoolean()    //to simulate fav
                        )
                    )
                }

                trendingList.value = tempList
            }
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