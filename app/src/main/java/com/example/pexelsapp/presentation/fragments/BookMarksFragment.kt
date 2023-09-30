package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pexelsapp.R
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.databinding.FragmentBookmarksBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.adapters.BookmarksPhotosAdapter
import com.example.pexelsapp.presentation.viewmodels.BookMarksViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookMarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private val bookmarksViewModel by viewModels<BookMarksViewModel>()
    private val bookmarksPhotosAdapter by lazy { BookmarksPhotosAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        showBottomNavigationView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        setupObservers()
        bookmarksViewModel.getSavedPhotosFromDataBase()
    }

    private fun setupViews() {

        val photoItemClick: (Int) -> Unit = { savedPhotoId ->
            val action = BookMarksFragmentDirections.actionIdBookmarksFragmentToIdDetailsFragment(
                AppConfig.getBookmarkFragmentName(),
                savedPhotoId
            )
            findNavController().navigate(action)
        }
        bookmarksPhotosAdapter.itemClick = photoItemClick
        binding.bookmarksPhotosRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(AppConfig.getSpanCount(), LinearLayoutManager.VERTICAL)
            adapter = bookmarksPhotosAdapter
        }
    }

    private fun setupListeners() {
        binding.noResultButton.setOnClickListener {
            val action = BookMarksFragmentDirections.actionIdBookmarksFragmentToIdHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        bookmarksViewModel.savedPhotosLiveData.observe(viewLifecycleOwner) { savedPhotos ->
            updateSavedPhotos(savedPhotos)
        }

        bookmarksViewModel.savedPhotosLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            changeProgressBarVisibility(isLoading)
        }

        bookmarksViewModel.loadingProgressLiveData.observe(viewLifecycleOwner) { loadingProgress ->
            binding.progressBar.progress = loadingProgress

        }
    }

    private fun changeProgressBarVisibility(isLoading: Boolean) {
        with(binding) {
            progressBar.isVisible = isLoading
            progressBar.progress = AppConfig.getEmptyInt()
        }
    }

    private fun updateSavedPhotos(photos: List<CuratedPhotoModel>) {
        if (photos.isNotEmpty()) {
            hideNoSavedPhotosViews()
        } else {
            showNoSavedPhotosViews()
        }
        bookmarksPhotosAdapter.updateSavedPhotos(photos)
    }

    private fun showNoSavedPhotosViews() {
        binding.noResultGroup.isVisible = true
    }

    private fun hideNoSavedPhotosViews() {
        binding.noResultGroup.isVisible = false
    }

    private fun showBottomNavigationView() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}