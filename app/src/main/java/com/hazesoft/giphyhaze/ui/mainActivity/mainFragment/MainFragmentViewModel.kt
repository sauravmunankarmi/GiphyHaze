package com.hazesoft.giphyhaze.ui.mainActivity.mainFragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hazesoft.giphyhaze.db.FavoriteGiphyGif
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import com.hazesoft.giphyhaze.util.App
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map
import okhttp3.internal.wait
import java.util.stream.Collectors
import kotlin.random.Random

/**
 * Created by Saurav
 * on 3/15/2022
 */
class MainFragmentViewModel(private val gifRepository: GifRepository): ViewModel() {

    val isLoading = MutableLiveData<Boolean>(true)
    val message = MutableLiveData<String>()

    var latestSearchString: String = ""

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("error: ${throwable.message}")
        isLoading.postValue(false)
        message.postValue("${throwable.localizedMessage.split(":").get(0)}. Please check your internet connection")
    }

    val favGiphyGifDbList: LiveData<List<FavoriteGiphyGif>> = gifRepository.allFavoritesGiphyGif.asLiveData()

    private val giphyGifApiList = MutableLiveData<ArrayList<GiphyGif>>(ArrayList())
    val favIds = ArrayList<String>()


    init{
        val job = CoroutineScope(Dispatchers.IO).launch{
            getFavIdsFromDb()
        }
    }

    suspend fun getFavIdsFromDb(){
        favIds.clear()
        runBlocking {
                gifRepository.allFavoritesGiphyGifList().forEach {
                    favIds.add(it.giphyId)
                }
        }
    }


    private val queryString = MutableLiveData<String>()

    val giphyGifDisplayList: LiveData<PagingData<GiphyGif>>  = queryString.switchMap { queryString ->

        if(queryString.isNotEmpty()){
            val response = gifRepository.getSearchedGifs(queryString).cachedIn(viewModelScope).map { pagingData ->
                pagingData.map {
                        giphyGif -> GiphyGif(giphyGif.id, giphyGif.images.downsized.url, giphyGif.id in favIds )
                }
            }
            isLoading.postValue(false)
            response
        }else{
            val response = gifRepository.getTrendingGifs().cachedIn(viewModelScope).map { pagingData ->
                pagingData.map {
                        giphyGif -> GiphyGif(giphyGif.id, giphyGif.images.downsized.url, giphyGif.id in favIds )
                }

            }
            isLoading.postValue(false)
            response
        }
    }




    fun getGif(searchString: String){
        latestSearchString = searchString
        if(searchString.isBlank()){
            queryString.postValue("")
        }else{
            queryString.postValue(searchString)
        }
    }

    fun reloadLastSearch(){

        val job = CoroutineScope(Dispatchers.IO).launch {
            getFavIdsFromDb()
            Dispatchers.Main{
                getGif(latestSearchString)
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