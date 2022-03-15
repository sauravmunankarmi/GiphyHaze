package com.hazesoft.giphyhaze.ui.mainActivity.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hazesoft.giphyhaze.R
import com.hazesoft.giphyhaze.adapter.GiphyGifListAdapter
import com.hazesoft.giphyhaze.databinding.FragmentFavoritesBinding
import com.hazesoft.giphyhaze.model.GiphyGif
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

        viewModel = ViewModelProvider(requireActivity()).get(FavoritesFragmentViewModel::class.java)

        setupUI()
        observeViewModel()

        viewModel.getAllFavoriteGiphyGif()
    }

    private fun setupUI(){

        giphyGifAdapter = GiphyGifListAdapter(requireContext(), this, "grid")
        binding.rvFavGiphyGif.apply {
            adapter = giphyGifAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

    }

    private fun observeViewModel(){
        viewModel.favoriteGiphyGifList.observe(requireActivity()) {favGifList ->
            favGifList?.let{
                giphyGifAdapter.differ.submitList(favGifList)
                giphyGifAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onFavClicked(giphyGif: GiphyGif) {

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            // Refresh tab data:
            if (requireActivity().supportFragmentManager != null) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}