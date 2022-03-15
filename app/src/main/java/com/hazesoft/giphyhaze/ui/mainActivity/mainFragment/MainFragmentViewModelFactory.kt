package com.hazesoft.giphyhaze.ui.mainActivity.mainFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hazesoft.giphyhaze.repository.GifRepository

/**
 * Created by Saurav
 * on 3/15/2022
 */
class MainFragmentViewModelFactory(private val repository: GifRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}