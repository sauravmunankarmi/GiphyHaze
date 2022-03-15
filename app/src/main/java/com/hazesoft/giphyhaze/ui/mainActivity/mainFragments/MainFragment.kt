package com.hazesoft.giphyhaze.ui.mainActivity.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.hazesoft.giphyhaze.R
import com.hazesoft.giphyhaze.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var viewModel: MainFragmentViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        viewModel.getTrendingGif()

    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}