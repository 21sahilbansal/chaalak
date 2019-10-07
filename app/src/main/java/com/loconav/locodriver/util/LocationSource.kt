package com.loconav.locodriver.util

interface LocationSource {
    companion object{
        const val LOCATION_WORKER_UPDATE_SERVICE = "location_worker_update_service"
        const val LOCATION_WORKER_LAST_KNOWN_LOCATION = "location_worker_last_known_location"
        const val TEST_CASE = "test_case"
    }
}