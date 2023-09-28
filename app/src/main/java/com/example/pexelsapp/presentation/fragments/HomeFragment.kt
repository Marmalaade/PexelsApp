package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.R
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.data.network.NetworkHelper
import com.example.pexelsapp.databinding.FragmentHomeBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import com.example.pexelsapp.presentation.adapters.CuratedPhotosAdapter
import com.example.pexelsapp.presentation.adapters.PopularRequestAdapter
import com.example.pexelsapp.presentation.viewmodels.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    private var requestsList = listOf<String>()
    private val popularRequestsAdapter by lazy { PopularRequestAdapter() }
    private val curatedPhotosAdapter by lazy { CuratedPhotosAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        showBottomNavigationView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        setupObservers()
        loadData()
    }

    private fun setupListeners() {
        binding.searchBar.onSearchListener = { query ->
            homeViewModel.setCurrentQuery(query)
            selectRequestPosition(query)
            homeViewModel.getPhotosByRequest(query)

        }

        binding.searchBar.onClearListener = {
            popularRequestsAdapter.updateSelectedItemPosition(AppConfig.getDefaultListPosition())
            homeViewModel.setCurrentQuery(AppConfig.getBaseRequest())
            homeViewModel.getCuratedPhotos()
        }

        binding.noResultsButton.setOnClickListener {
            binding.searchBar.clearTextField()
            popularRequestsAdapter.updateSelectedItemPosition(AppConfig.getDefaultListPosition())
            homeViewModel.setCurrentQuery(AppConfig.getBaseRequest())
            homeViewModel.getCuratedPhotos()
        }

        binding.noInternetIcon.setOnClickListener {
            if (homeViewModel.getCurrentQuery() == AppConfig.getBaseRequest()) homeViewModel.getCuratedPhotos()
            else homeViewModel.getPhotosByRequest(homeViewModel.getCurrentQuery())
        }
    }

    private fun loadData() {
        homeViewModel.getPopularRequests()
        if (homeViewModel.getCurrentQuery() == AppConfig.getBaseRequest()) homeViewModel.getCuratedPhotos()
        else binding.searchBar.setRequestText(homeViewModel.getCurrentQuery())
    }

    private fun selectRequestPosition(query: String) {
        requestsList.indexOfFirst { it == query }
            .takeIf { it != AppConfig.getDefaultListPosition() }
            ?.let { popularRequestsAdapter.updateSelectedItemPosition(it) }
    }

    private fun setupViews() {
        setupAdapters(setupClickListeners())
    }

    private fun setupClickListeners(): Pair<(String, Int) -> Unit, (Int) -> Unit> {
        val requestItemClick: (String, Int) -> Unit = { requestTittle, itemPosition ->
            popularRequestsAdapter.updateSelectedItemPosition(itemPosition)
            homeViewModel.setCurrentQuery(requestTittle)
            binding.searchBar.setRequestText(requestTittle)

            if (itemPosition != 0) {
                Collections.swap(requestsList, itemPosition, 0)
                popularRequestsAdapter.updateRequests(requestsList.map { RequestModel(it) })
                popularRequestsAdapter.updateSelectedItemPosition(0)
                binding.popularRequestRecycler.scrollToPosition(0)
            }
        }
        val photoItemClick: (Int) -> Unit = { selectedPhotoId ->
            val action =
                HomeFragmentDirections.actionIdHomeFragmentToIdDetailsFragment(AppConfig.getHomeFragmentName(), selectedPhotoId)
            findNavController().navigate(action)
        }

        return Pair(requestItemClick, photoItemClick)
    }

    private fun setupAdapters(clickListeners: Pair<(String, Int) -> Unit, (Int) -> Unit>) {
        popularRequestsAdapter.itemClick = clickListeners.first
        binding.popularRequestRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularRequestsAdapter
        }

        curatedPhotosAdapter.itemClick = clickListeners.second
        binding.curatedPhotosRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(AppConfig.getSpanCount(), LinearLayoutManager.VERTICAL)
            adapter = curatedPhotosAdapter
        }
    }

    private fun setupObservers() {
        homeViewModel.popularRequestsLiveData.observe(viewLifecycleOwner) { popularRequests ->
            updateRequests(popularRequests)
        }

        homeViewModel.curatedPhotosLiveData.observe(viewLifecycleOwner) { curatedPhotos ->
            updatePhotos(curatedPhotos)
        }

        homeViewModel.photosByRequestLiveData.observe(viewLifecycleOwner) { photos ->
            updatePhotos(photos)
        }

    }

    private fun updatePhotos(photos: List<CuratedPhotoModel>) {
        if (photos.isNotEmpty()) {
            if (!context?.let { NetworkHelper(it).isNetworkAvailable() }!!) {
                showNoInternetConnectionToast()
            }
            hideNoResultsViews()
        } else {
            showNoResultsViews()
        }
        curatedPhotosAdapter.updateCuratedPhotos(photos)
    }

    private fun showNoInternetConnectionToast() {
        val message = getString(R.string.no_internet_connection)
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun hideNoResultsViews() {
        binding.noResultsButton.isVisible = false
        binding.noResultsTextView.isVisible = false
    }

    private fun showNoResultsViews() {
        binding.noResultsButton.isVisible = true
        binding.noResultsTextView.isVisible = true
    }

    private fun updateRequests(requestsData: List<RequestModel>) {
        requestsList = requestsData.map { requestModel -> requestModel.title }
        if (requestsData.isNotEmpty()) popularRequestsAdapter.updateRequests(requestsData)
        selectRequestPosition(homeViewModel.getCurrentQuery())
    }

    private fun showBottomNavigationView() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.searchBar.onDestroy()
        _binding = null

    }
}
