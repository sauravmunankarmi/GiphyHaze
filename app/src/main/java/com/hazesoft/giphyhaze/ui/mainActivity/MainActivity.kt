package com.hazesoft.giphyhaze.ui.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.hazesoft.giphyhaze.adapter.TabLayoutAdapter
import com.hazesoft.giphyhaze.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI(){
        //setting up tabbed layouts
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText("Home")
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText("Favorites")
        )

        val adapter = TabLayoutAdapter(supportFragmentManager, binding.tabLayout.tabCount)

        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


    }

    fun setCurrentTab(position: Int) {
        binding.viewPager.currentItem = position
    }

}