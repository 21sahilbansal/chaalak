package com.loconav.locodriver.util

import com.loconav.locodriver.driver.model.Address


class AddressUtil {
    companion object {
        private val stateMap = hashMapOf(
            0 to "Andhra Pradesh"
            , 1 to "Arunachal Pradesh"
            , 2 to "Assam"
            , 3 to "Bihar"
            , 4 to "Chhattisgarh"
            , 5 to "Goa"
            , 6 to "Gujarat"
            , 7 to "Haryana"
            , 8 to "Himachal Pradesh"
            , 9 to "Jammu and Kashmir"
            , 10 to "Jharkhand"
            , 11 to "Karnataka"
            , 12 to "Kerala"
            , 13 to "Maharashtra"
            , 14 to "Manipur"
            , 15 to "Meghalaya"
            , 16 to "Mizoram"
            , 17 to "Nagaland"
            , 18 to "Odisha"
            , 19 to "Punjab"
            , 20 to "Sikkim"
            , 21 to "Rajasthan"
            , 22 to "Tamil Nadu"
            , 23 to "Telangana"
            , 24 to "Tripura"
            , 25 to "Uttar Pradesh"
            , 26 to "Uttarakhand"
            , 27 to "West Bengal"
            , 28 to "Madhya Pradesh"
            , 29 to "Andaman and Nicobar Islands"
            , 30 to "Chandigarh"
            , 31 to "Dadar and Nagar Haveli"
            , 32 to "Daman and Diu"
            , 33 to "Delhi"
            , 34 to "Lakshadweep"
            , 35 to "Puducherry"
        )

        private fun getState(stateMapkey: Int?): String {
            stateMapkey?.let {
                if (stateMap.containsKey(it)) {
                    return stateMap[it].toString()
                }
            }
            return ""
        }

        fun getAddress(addressAttribute: Address): String {
            return String.format(
                "%s,%s,%s,%s,%s,%s - %s"
                , addressAttribute.houseNumber
                , addressAttribute.addressLine1
                , addressAttribute.addressLine2
                , addressAttribute.addressLine3
                , addressAttribute.city
                , getState(addressAttribute.state)
                , addressAttribute.pincode
            )
        }
    }
}