package com.hazesoft.giphyhaze.ui.mainActivity.mainFragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.hazesoft.giphyhaze.db.FavoriteGiphyGif
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import com.hazesoft.giphyhaze.util.App
import kotlinx.coroutines.*
import java.util.stream.Collectors
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
        isLoading.postValue(false)
        message.postValue("${throwable.localizedMessage.split(":").get(0)}. Please check your internet connection")
    }

    val favGiphyGifDbList: LiveData<List<FavoriteGiphyGif>> = gifRepository.allFavoritesGiphyGif.asLiveData()


    private val giphyGifApiList = MutableLiveData<ArrayList<GiphyGif>>(ArrayList())
//    val favGiphyGifDbList: LiveData<List<FavoriteGiphyGif>> = gifRepository.allFavoritesGiphyGif.asLiveData()

    val giphyGifDisplayList = MutableLiveData<ArrayList<GiphyGif>>(ArrayList())






    fun getTrendingGif() {
        isLoading.postValue(true)
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val favIds = ArrayList<String>()
            gifRepository.allFavoritesGiphyGifList().forEach {
                favIds.add(it.giphyId)
            }
            println("favIds: ${favIds}")
            val response = gifRepository!!.getTrendingGifs()
            withContext(Dispatchers.Main) {
                if(response.isSuccessful){
                    val tempList = ArrayList<GiphyGif>()
                    response.body()?.data?.forEach {
                        tempList.add(
                            GiphyGif(
                                it.id,
                                it.images.downsized.url,
                                it.id in favIds
                            )
                        )
                        println("tempList : ${tempList}")
                    }
                    isLoading.postValue(false)
                    giphyGifDisplayList.value = tempList
                }else{
                    isLoading.postValue(false)
                    message.postValue("Unexpected error occurred while retrieving gif, please try again")
                }


            }
        }
    }



    fun updateFavGifOfCurrentList(){
        val favIds = ArrayList<String>()
        favGiphyGifDbList.value?.forEach {
            favIds.add(it.giphyId)
        }

        val temp = giphyGifDisplayList.value
        temp?.forEach {
            it.isFavorite = it.giphyId in favIds
        }

        giphyGifDisplayList.value = temp ?: ArrayList()
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