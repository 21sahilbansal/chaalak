package com.loconav.locodriver.base


abstract class PubSubEvent<T> @JvmOverloads constructor(message: String, `object`: T? = null) {

    var message: String
        protected set

    var `object`: Any? = null
        protected set

    init {
        this.message = message
        this.`object` = `object`
    }
}