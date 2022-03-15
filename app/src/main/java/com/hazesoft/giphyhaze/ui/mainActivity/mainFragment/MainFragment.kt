package com.hazesoft.giphyhaze.ui.mainActivity.mainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hazesoft.giphyhaze.adapter.GiphyGifListAdapter
import com.hazesoft.giphyhaze.databinding.FragmentMainBinding
import com.hazesoft.giphyhaze.model.GiphyGif
import com.hazesoft.giphyhaze.util.App


class MainFragment : Fragment(), GiphyGifListAdapter.OnFavoriteToggleClicked {

    private lateinit var viewModel: MainFragmentViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var giphyGifAdapter: GiphyGifListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, MainFragmentViewModelFactory((requireActivity().application as App).repository)).get(MainFragmentViewModel::class.java)

        setupUI()
        observeViewModel()
        viewModel.getTrendingGif()

    }

    private fun setupUI(){

        giphyGifAdapter = GiphyGifListAdapter(requireContext(), this, "linear")
        binding.rvGiphyGif.apply {
            adapter = giphyGifAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun observeViewModel(){
        viewModel.giphyGifDisplayList.observe(requireActivity()) { giphyGifList ->
            giphyGifList?.let{
                giphyGifAdapter.differ.submitList(giphyGifList)
                giphyGifAdapter?.notifyDataSetChanged()
            }

        }
    }

    override fun onFavClicked(giphyGif: GiphyGif, type: String) {
        if(type == "linear"){
            viewModel.favToggle(giphyGif)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}