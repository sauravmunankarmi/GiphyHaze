package com.hazesoft.giphyhaze.ui.mainActivity.mainFragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import kotlinx.coroutines.*
import kotlin.random.Random

/**
 * Created by Saurav
 * on 3/15/2022
 */
class MainFragmentViewModel: ViewModel() {

    private val gifRepository = GifRepository()

    private var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("error: ${throwable.message}")
    }


    val trendingList = MutableLiveData<ArrayList<GiphyGif>>(ArrayList())




    fun getTrendingGif(){
        println("start")
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = gifRepository.getTrendingGifs()
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
}