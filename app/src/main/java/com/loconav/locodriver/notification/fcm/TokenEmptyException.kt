package com.loconav.locodriver.notification.fcm

class TokenEmptyException(errorMessage : String) : Exception(errorMessage){
    companion object{
        const val TOKEN_IS_NULL = "token is null";
    }
}