package com.example.pexelsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.pexelsapp.R
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.databinding.FragmentDetailsBinding
import com.example.pexelsapp.presentation.viewmodels.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private val arguments: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        hideBottomNavigationView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
        checkDestinations()

    }

    private fun hideBottomNavigationView() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        homeViewModel.selectedPhotoLiveData.observe(viewLifecycleOwner) { selectedPhoto ->
            binding.authorsInformation.text = selectedPhoto.photographer

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .timeout(AppConfig.getCachingTimeout())
            Glide
                .with(this)
                .load(selectedPhoto.url)
                .apply(requestOptions)
                .into(binding.selectedPhotoItem)
        }


    }

    private fun checkDestinations() {
        if (arguments.fragmentName == AppConfig.getHomeFragmentName()) homeViewModel.getSelectedPhoto(arguments.selectedPhotoId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
