package com.hazesoft.giphyhaze.ui.mainActivity.mainFragment

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.hazesoft.giphyhaze.db.FavoriteGiphyGif
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.repository.GifRepository
import kotlinx.coroutines.*

/**
 * Created by Saurav
 * on 3/15/2022
 */
class MainFragmentViewModel(private val gifRepository: GifRepository): ViewModel() {

    val isLoading = MutableLiveData(true)
    val message = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        isLoading.postValue(false)
        message.postValue("${throwable.localizedMessage.split(":").get(0)}. Please check your internet connection")
    }

    //observing db changes for favorite gif add/remove;
    val favGiphyGifDbList: LiveData<List<FavoriteGiphyGif>> = gifRepository.allFavoritesGiphyGif.asLiveData()

    private val favIds = ArrayList<String>()

    private val queryString = MutableLiveData<String>() //to trigger api call

    //temporarily store last searched term
    //it is used to refresh/update the paged homeFragment gif list to display changes when user removes fav from FavoritesFragment or adds new fav
    var latestSearchString: String = ""

    init{
        //first thing we do is we check local db for favorite gifs and get the favIds
        favIds.clear()
        CoroutineScope(Dispatchers.IO).launch{
            runBlocking {
                gifRepository.allFavoritesGiphyGifList()?.forEach {
                    favIds.add(
                        it.giphyId
                    )
                }
            }
        }
    }

    //getting paged data from api call :: ui will observe this field to display gif in home fragment
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

    //function to trigger appropriate api call
    fun getGif(searchString: String){
        latestSearchString = searchString
        if(searchString.isBlank()){
            queryString.postValue("")
        }else{
            queryString.postValue(searchString)
        }
    }

    //fun to reload last search to make the button status in main gif list up-to-date
    fun reloadLastSearch(){
        favIds.clear()
        favGiphyGifDbList.value?.forEach {
            favIds.add(it.giphyId)
        }
        getGif(latestSearchString)
    }

    //fun to change fav status of a gif when user clicks the fav action button
    fun favToggle(giphyGif: GiphyGif){

        if(giphyGif.isFavorite){
            //it was favorite now remove from favorite
            println("it was favorite now remove because: giphyGif.isFavorite ${giphyGif.isFavorite}")
            removeFavoriteGiphyGifFromDb(giphyGif)
        }else{
            //it was not favorite before, now it is favorite so add
            println("it was not favorite now it is fav because: giphyGif.isFavorite ${giphyGif.isFavorite}")
            addFavoriteGiphyGifInDb(giphyGif)
        }
    }

    private fun removeFavoriteGiphyGifFromDb(giphyGif: GiphyGif){
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            gifRepository!!.removeFavoriteGiphyGif(giphyGif)
        }
    }

    private fun addFavoriteGiphyGifInDb(giphyGif: GiphyGif){
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            gifRepository!!.addFavoriteGiphyGif(giphyGif)
        }
    }
}