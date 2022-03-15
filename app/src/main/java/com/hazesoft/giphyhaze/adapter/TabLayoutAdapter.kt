package com.hazesoft.giphyhaze.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hazesoft.giphyhaze.ui.mainActivity.mainFragments.FavoritesFragment
import com.hazesoft.giphyhaze.ui.mainActivity.mainFragments.MainFragment

/**
 * Created by Saurav
 * on 3/15/2022
 */
class TabLayoutAdapter(private val fragmentManager: FragmentManager,
                       private val totalItem: Int,): FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return totalItem
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> {
                return MainFragment()
            }

            1 -> {
                return FavoritesFragment()
            }

            else -> {
                return MainFragment()
            }
        }
    }
}