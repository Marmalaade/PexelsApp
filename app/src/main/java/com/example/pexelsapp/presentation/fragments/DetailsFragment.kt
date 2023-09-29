package com.example.pexelsapp.presentation.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.isVisible
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
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.generics.AnimationHelper
import com.example.pexelsapp.presentation.viewmodels.DetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.io.BufferedInputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel by viewModels<DetailsViewModel>()
    private val arguments: DetailsFragmentArgs by navArgs()
    private var currentPhoto: CuratedPhotoModel? = null
    private var animationHelper: AnimationHelper<ImageButton>? = null

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
        checkDestinationsAndData()
    }

    private fun hideBottomNavigationView() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            animationHelper = AnimationHelper(binding.backButton)
            animationHelper?.apply {
                cancelAnimation()
                animateScaleDownAndUp(0.8f, 50)
            }
            if (arguments.fragmentName == AppConfig.getHomeFragmentName()) {
                val action = DetailsFragmentDirections.actionDetailsFragmentToHomeFragment()

                findNavController().navigate(action)
            }
        }

        binding.bookmarkButton.setOnClickListener {
            currentPhoto?.let { photo ->
                detailsViewModel.insertPhotosInDB(photo)
            }
        }

        binding.bookmarkButtonActive.setOnClickListener {
            currentPhoto?.id?.let { id ->
                detailsViewModel.deletePhotoFromDataBase(id)
            }
        }

        binding.downloadButton.setOnClickListener {
            animationHelper = AnimationHelper(binding.downloadButton)
            animationHelper?.apply {
                cancelAnimation()
                animateScaleDownAndUp(0.8f, 100)
            }
            downloadImageToDevice()

        }
    }

    private fun downloadImageToDevice() {
        currentPhoto?.url?.let { url ->
            Single.create<Boolean> { emitter ->
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()
                    val inputStream = connection.inputStream
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val bitmap = BitmapFactory.decodeStream(bufferedInputStream)

                    val filename = System.currentTimeMillis().toString() + ".jpg"
                    var fos: OutputStream? = null

                    val contentResolver = requireContext().contentResolver

                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(
                            MediaStore.Images.Media.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES
                        )
                    }

                    val imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    fos = imageUri?.let { contentResolver.openOutputStream(it) }

                    fos?.use {
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        emitter.onSuccess(true)
                    } ?: emitter.onSuccess(false)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emitter.onSuccess(false)
                }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { saved ->
                        val messageResId = if (saved) R.string.saved_to_gallery else R.string.error_saving
                        Toast.makeText(
                            requireContext(),
                            getString(messageResId),
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    { error ->
                        error.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error_saving),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
        }
    }

    private fun loadSelectedImage(selectedPhotoUrl: String) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(true)
            .timeout(AppConfig.getCachingTimeout())

        Glide.with(this)
            .load(selectedPhotoUrl)
            .apply(requestOptions)
            .into(binding.selectedPhotoItem)
    }


    private fun setupObservers() {
        detailsViewModel.selectedPhotoLiveData.observe(viewLifecycleOwner) { selectedPhoto ->
            updatePhoto(selectedPhoto)
        }

        detailsViewModel.ifPhotoInDataBaseLiveData.observe(viewLifecycleOwner) { ifPhotoInDataBase ->
            with(binding) {
                bookmarkButton.isVisible = !ifPhotoInDataBase
                bookmarkButtonActive.isVisible = ifPhotoInDataBase
            }
        }

        detailsViewModel.detailsPhotoLoadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            changeProgressBarVisibility(isLoading)
        }

        detailsViewModel.loadingProgressLiveData.observe(viewLifecycleOwner) { loadingProgress ->
            binding.progressBar.progress = loadingProgress

        }

    }

    private fun changeProgressBarVisibility(isLoading: Boolean) {
        with(binding) {
            progressBar.isVisible = isLoading
            progressBar.progress = 0
        }
    }

    private fun updatePhoto(photo: CuratedPhotoModel?) {
        currentPhoto = photo
        with(binding) {
            if (photo == null) {
                noImageButton.isVisible = true
                noImageTextView.isVisible = true
                authorsInformation.isVisible = false
                bottomFunctionalButtons.isVisible = false
            } else {
                noImageButton.isVisible = false
                noImageTextView.isVisible = false
                authorsInformation.isVisible = true
                bottomFunctionalButtons.isVisible = true
                authorsInformation.text = photo.photographer
                loadSelectedImage(photo.url)
            }
        }
    }

    private fun checkDestinationsAndData() {
        if (arguments.fragmentName == AppConfig.getHomeFragmentName()) detailsViewModel.getSelectedPhoto(arguments.selectedPhotoId)
        else {
            detailsViewModel.getPhotoFromDataBase(arguments.selectedPhotoId)
        }
        detailsViewModel.getPhotoFromDataBase(arguments.selectedPhotoId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
