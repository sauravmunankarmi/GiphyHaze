package com.hazesoft.giphyhaze.ui.mainActivity.mainFragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
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
        viewModel.getGif("")

    }

    private fun setupUI(){

        giphyGifAdapter = GiphyGifListAdapter(requireContext(), this)
        binding.rvGiphyGif.apply {
            adapter = giphyGifAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        //search view listeners
        binding.svSearchGif.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                viewModel.getGif(p0 ?: "")
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                if(TextUtils.isEmpty(p0)){
                    //show trending list when search bar is empty/cleared
                    viewModel.getGif("")
                }
                return true
            }
        })
    }

    private fun observeViewModel(){
        viewModel.isLoading.observe(requireActivity()) {isLoading ->
            isLoading?.let{
                if(isLoading){
                    binding.loadingMainFrag.visibility = View.VISIBLE
                    binding.rvGiphyGif.visibility = View.GONE
                }else{
                    binding.loadingMainFrag.visibility = View.GONE
                    binding.rvGiphyGif.visibility = View.VISIBLE
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

        viewModel.giphyGifDisplayList.observe(viewLifecycleOwner) { giphyGifList ->
            giphyGifList?.let{
                giphyGifAdapter.submitData(viewLifecycleOwner.lifecycle, giphyGifList)
            }
        }

        viewModel.favGiphyGifDbList.observe(requireActivity()) { favDbList ->
            favDbList?.let{
                //when ever db is updated, trigger reload to show latest changes
                viewModel.reloadLastSearch()
            }
        }
    }

    override fun onFavClicked(giphyGif: GiphyGif) {
        val msg = if(giphyGif.isFavorite){
            "GIF removed from Favorites"
        }else{
            "Added to Favorites"
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

        viewModel.favToggle(giphyGif)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}