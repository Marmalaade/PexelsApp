package com.example.pexelsapp.data.apimodels

import com.squareup.moshi.Json

data class CuratedPhotoResponse(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "src") val src: OriginalPhotoResponse? = null,
    @Json(name = "photographer") val photographer: String? = null,
)