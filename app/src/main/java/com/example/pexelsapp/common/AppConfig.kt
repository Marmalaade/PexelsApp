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
        private const val HOME_FRAGMENT_NAME = "home_fragment"
        private const val BOOKMARK_FRAGMENT_NAME = "home_fragment"
        private const val DATABASE_NAME = "photos_database"
        private const val CACHE_CONTROL = "Cache-Control"
        private const val CACHE_SUBDIRECTORY = "http-cache"
        private const val PROGRESS_DELAY = 30
        private const val MAX_PROGRESS = 100
        fun getDataBaseName() = DATABASE_NAME
        fun getHomeFragmentName() = HOME_FRAGMENT_NAME
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
        fun getCacheControl() = CACHE_CONTROL
        fun getCacheSubDir() = CACHE_SUBDIRECTORY
        fun getBookmarkFragmentName() = BOOKMARK_FRAGMENT_NAME
        fun getProgressDelay() = PROGRESS_DELAY
        fun getMaxProgress() = MAX_PROGRESS
    }
}

