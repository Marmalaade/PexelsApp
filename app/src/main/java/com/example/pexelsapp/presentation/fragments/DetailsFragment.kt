package com.example.pexelsapp.presentation.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.pexelsapp.data.database.PhotosDataDataBase
import com.example.pexelsapp.databinding.FragmentDetailsBinding
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.viewmodels.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private val arguments: DetailsFragmentArgs by navArgs()
    private var currentPhoto: CuratedPhotoModel? = null

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
        homeViewModel.getAllPhotosFromDataBase()
    }

    private fun hideBottomNavigationView() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            if (arguments.fragmentName == AppConfig.getHomeFragmentName()) {
                val action = DetailsFragmentDirections.actionDetailsFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }

        binding.bookmarkButton.setOnClickListener {
            currentPhoto?.let { photo ->
                homeViewModel.insertPhotosInDB(photo)
            }
        }

        binding.bookmarkButtonActive.setOnClickListener {
            currentPhoto?.id?.let { id ->
                homeViewModel.deletePhotoFromDataBase(id)
            }
        }

        binding.downloadButton.setOnClickListener {
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
        Glide
            .with(this)
            .load(selectedPhotoUrl)
            .apply(requestOptions)
            .into(binding.selectedPhotoItem)
    }

    private fun setupObservers() {
        homeViewModel.selectedPhotoLiveData.observe(viewLifecycleOwner) { selectedPhoto ->
            updatePhoto(selectedPhoto)
        }

        homeViewModel.ifPhotoInDataBaseLiveData.observe(viewLifecycleOwner) { ifPhotoInDataBase ->
            Log.e("flag", "$ifPhotoInDataBase")
            with(binding) {
                bookmarkButton.isVisible = !ifPhotoInDataBase
                bookmarkButtonActive.isVisible = ifPhotoInDataBase
            }
        }

        homeViewModel.mama.observe(viewLifecycleOwner) { photos ->
            for (el in photos) {
                Log.e("el ", "$el")
            }
        }
    }

    private fun updatePhoto(photo: CuratedPhotoModel?) {
        currentPhoto = photo
        with(binding) {
            if (photo == null) {
                noImageButton.isVisible = true
                noImageTextView.isVisible = true
                authorsInformation.isVisible = false
                selectedPhotoScrollView.isVisible = false
                bottomFunctionalButtons.isVisible = false
            } else {
                noImageButton.isVisible = false
                noImageTextView.isVisible = false
                authorsInformation.isVisible = true
                selectedPhotoScrollView.isVisible = true
                bottomFunctionalButtons.isVisible = true
                authorsInformation.text = photo.photographer

                loadSelectedImage(photo.url)
            }
        }
    }

    private fun checkDestinations() {
        Log.e("argid", "${arguments.selectedPhotoId}")
        if (arguments.fragmentName == AppConfig.getHomeFragmentName()) homeViewModel.getSelectedPhoto(arguments.selectedPhotoId)
        else {
            homeViewModel.getPhotoFromDataBase(arguments.selectedPhotoId)
        }
        homeViewModel.getPhotoFromDataBase(arguments.selectedPhotoId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
