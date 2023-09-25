package com.example.pexelsapp.common

class AppConfig {
    companion object {
        private const val API_KEY = "Oh678CDV4t4oYLeGpQQn1Ea4hwFG8HD53GG3g3FXlYrCZ3VGtaRrk4Qx"
        private const val REQUESTS_PER_PAGE = 7
        private const val PHOTOS_PER_PAGE = 30
        private const val EMPTY_INT =0
        private const val EMPTY_STRING=""

        fun getApiKey() = API_KEY
        fun getRequestsPerPage() = REQUESTS_PER_PAGE
        fun getPhotosPerPage() = PHOTOS_PER_PAGE
        fun getEmptyInt() = EMPTY_INT
        fun getEmptyString() = EMPTY_STRING

    }
}