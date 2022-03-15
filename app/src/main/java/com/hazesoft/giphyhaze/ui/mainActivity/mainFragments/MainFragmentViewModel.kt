package com.hazesoft.giphyhaze.ui.mainActivity.mainFragments

import androidx.lifecycle.ViewModel
import com.hazesoft.giphyhaze.repository.GifRepository
import kotlinx.coroutines.*

/**
 * Created by Saurav
 * on 3/15/2022
 */
class MainFragmentViewModel: ViewModel() {

    val gifRepository = GifRepository()

    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("error: ${throwable.message}")
    }



    fun getTrendingGif(){
        println("start")
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = gifRepository.getTrendingGifs()
            withContext(Dispatchers.Main){
                println("success?")
            }
        }
    }
}