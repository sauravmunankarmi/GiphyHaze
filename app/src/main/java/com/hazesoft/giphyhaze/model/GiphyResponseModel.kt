package com.hazesoft.giphyhaze.model

import com.google.gson.annotations.SerializedName

data class GiphyResponseModel(
    val `data`: ArrayList<Data>,
    val meta: Meta,
    val pagination: Pagination
) {
    data class Data(
        val id: String,
        val images: Images
    ) {
        data class Images(
            val downsized: Downsized
        ) {
            data class Downsized(
                val height: String,
                val size: String,
                val url: String,
                val width: String
            )
        }
    }

    data class Meta(
        val msg: String,
        val response_id: String,
        val status: Int
    )

    data class Pagination(
        val count: Int,
        val offset: Int,
        val total_count: Int
    )
}