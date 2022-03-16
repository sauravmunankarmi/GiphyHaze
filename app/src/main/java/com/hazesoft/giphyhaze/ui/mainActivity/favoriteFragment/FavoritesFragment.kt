package com.hazesoft.giphyhaze.ui.mainActivity.favoriteFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hazesoft.giphyhaze.adapter.FavGiphyGifListAdapter
import com.hazesoft.giphyhaze.adapter.GiphyGifListAdapter
import com.hazesoft.giphyhaze.databinding.FragmentFavoritesBinding
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.util.App


class FavoritesFragment : Fragment(), FavGiphyGifListAdapter.OnFavoriteToggleClicked {

    private lateinit var viewModel: FavoritesFragmentViewModel
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favGiphyGifAdapter: FavGiphyGifListAdapter

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

        favGiphyGifAdapter = FavGiphyGifListAdapter(requireContext(), this)
        binding.rvFavGiphyGif.apply {
            adapter = favGiphyGifAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }

    private fun observeViewModel(){
        viewModel.favoriteGiphyGifDisplayList.observe(requireActivity()) {favGifList ->
            favGifList?.let{
                favGiphyGifAdapter.differ.submitList(favGifList)
                favGiphyGifAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onFavClicked(giphyGif: GiphyGif) {

        viewModel.removeFavoriteGiphyGifFromDb(giphyGif)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}