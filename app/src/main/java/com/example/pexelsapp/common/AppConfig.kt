package com.example.pexelsapp.common

class AppConfig {
    companion object {
        private const val API_KEY = "Oh678CDV4t4oYLeGpQQn1Ea4hwFG8HD53GG3g3FXlYrCZ3VGtaRrk4Qx"
        private const val REQUESTS_PER_PAGE = 7
        private const val PHOTOS_PER_PAGE = 30
        private const val EMPTY_INT = 0
        private const val EMPTY_STRING = ""
        private const val AUTOMATIC_SEARCH_DELAY_MS = 500L
        private const val SPAN_COUNT = 2
        private const val BASE_REQUEST = "popular_photos"
        private const val DEFAULT_LIST_POSITION = -1
        private const val CACHING_TIMEOUT = 3600000
        fun getSpanCount() = SPAN_COUNT

        fun getCachingTimeout() = CACHING_TIMEOUT
        fun getAutomaticSearchDelay() = AUTOMATIC_SEARCH_DELAY_MS
        fun getApiKey() = API_KEY
        fun getRequestsPerPage() = REQUESTS_PER_PAGE
        fun getPhotosPerPage() = PHOTOS_PER_PAGE
        fun getEmptyInt() = EMPTY_INT
        fun getEmptyString() = EMPTY_STRING
        fun getBaseRequest() = BASE_REQUEST
        fun getDefaultListPosition() = DEFAULT_LIST_POSITION
    }

}
