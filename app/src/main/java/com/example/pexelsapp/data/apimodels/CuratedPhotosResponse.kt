package com.example.pexelsapp.data.apimodels

import com.squareup.moshi.Json

data class CuratedPhotosResponse(
    @Json(name = "photos") val photos: List<CuratedPhotoResponse>? = null,
)
