package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.databinding.FragmentHomeBinding
import com.example.pexelsapp.presentation.adapters.CuratedPhotosAdapter
import com.example.pexelsapp.presentation.adapters.PopularRequestAdapter
import com.example.pexelsapp.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()
    private val popularRequestsAdapter by lazy { PopularRequestAdapter() }
    private val curatedPhotosAdapter by lazy { CuratedPhotosAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.popularRequestRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularRequestsAdapter
        }
        binding.curatedPhotosRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            adapter = curatedPhotosAdapter
        }
    }

    private fun observeViewModel() {
        homeViewModel.requestsLiveData.observe(viewLifecycleOwner) { requests ->
            popularRequestsAdapter.updateRequests(requests)
        }
        homeViewModel.curatedPhotosLiveData.observe(viewLifecycleOwner) { photos ->
            curatedPhotosAdapter.updateCuratedPhotos(photos)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
