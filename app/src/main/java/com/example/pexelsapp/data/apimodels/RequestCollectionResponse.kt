package com.example.pexelsapp.data.apimodels

import com.squareup.moshi.Json

data class RequestCollectionResponse(
    @Json(name = "collections") val collections: List<RequestResponse>? = null
)