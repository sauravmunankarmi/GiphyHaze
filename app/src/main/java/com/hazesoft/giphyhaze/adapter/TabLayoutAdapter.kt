package com.hazesoft.giphyhaze.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hazesoft.giphyhaze.ui.mainActivity.favoriteFragment.FavoritesFragment
import com.hazesoft.giphyhaze.ui.mainActivity.mainFragment.MainFragment

/**
 * Created by Saurav
 * on 3/15/2022
 */
class TabLayoutAdapter(
    fragmentManager: FragmentManager,
    private val totalItem: Int,): FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return totalItem
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                MainFragment()
            }

            1 -> {
                FavoritesFragment()
            }

            else -> {
                MainFragment()
            }
        }
    }
}