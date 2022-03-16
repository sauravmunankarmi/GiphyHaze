package com.hazesoft.giphyhaze.ui.mainActivity.favoriteFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hazesoft.giphyhaze.adapter.FavGiphyGifListAdapter
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

        viewModel = ViewModelProvider(this, FavoritesFragmentViewModelFactory((requireActivity().application as App).repository)).get(FavoritesFragmentViewModel::class.java)

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
                if(favGifList.isEmpty()){

//                    Snackbar.make(binding.root, "Oops!! Looks like yo do not have any favorite GIFs.", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("OK"){
//                            (activity as MainActivity)?.setCurrentTab(0)  //go to home tab if no favorites
//                        }
//                        .show()

                    binding.rvFavGiphyGif.visibility = View.GONE
                    binding.tvFavMsg.visibility = View.VISIBLE
                    binding.tvFavMsg.text = "No favorite GIFs.\nStart adding favorite gif from home tab :)"

                }else{
                    binding.rvFavGiphyGif.visibility = View.VISIBLE
                    binding.tvFavMsg.visibility = View.GONE
                }
            }
        }

        viewModel.message.observe(requireActivity()) {msg ->
            msg?.let{
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK"){}
                    .show()
            }
        }
    }

    override fun onFavRemoveClicked(giphyGif: GiphyGif) {
        Toast.makeText(requireContext(), "GIF removed from Favorites", Toast.LENGTH_SHORT).show()
        viewModel.removeFavoriteGiphyGifFromDb(giphyGif)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}