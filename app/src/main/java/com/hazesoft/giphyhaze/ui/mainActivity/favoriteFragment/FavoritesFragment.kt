package com.hazesoft.giphyhaze.ui.mainActivity.favoriteFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hazesoft.giphyhaze.adapter.GiphyGifListAdapter
import com.hazesoft.giphyhaze.databinding.FragmentFavoritesBinding
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.ui.mainActivity.mainFragment.MainFragmentViewModel
import com.hazesoft.giphyhaze.ui.mainActivity.mainFragment.MainFragmentViewModelFactory
import com.hazesoft.giphyhaze.util.App


class FavoritesFragment : Fragment(), GiphyGifListAdapter.OnFavoriteToggleClicked {

    private lateinit var viewModel: FavoritesFragmentViewModel
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var giphyGifAdapter: GiphyGifListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, FavoritesFragmentViewModelFactory((requireActivity().application as App).repository)).get(
            FavoritesFragmentViewModel::class.java)

        setupUI()
        observeViewModel()

    }

    private fun setupUI(){

        giphyGifAdapter = GiphyGifListAdapter(requireContext(), this, "grid")
        binding.rvFavGiphyGif.apply {
            adapter = giphyGifAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }

    private fun observeViewModel(){
        viewModel.favoriteGiphyGifDisplayList.observe(requireActivity()) {favGifList ->
            favGifList?.let{
                giphyGifAdapter.differ.submitList(favGifList)
                giphyGifAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onFavClicked(giphyGif: GiphyGif) {

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}