package com.hazesoft.giphyhaze.model

/**
 * Created by Saurav
 * on 3/15/2022
 */
data class GiphyGif(
    //unique identifier provided by giphy
    val giphyId: String,
    //display url for gif
    val giphyGifUrl: String,
    //flag to indicate if the gif is marked as favorite in local db or not
    var isFavorite: Boolean
)
