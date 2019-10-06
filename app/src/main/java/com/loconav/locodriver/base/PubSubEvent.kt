package com.loconav.locodriver.base


abstract class PubSubEvent @JvmOverloads constructor(message: String, `object`: Any? = null) {

    var message: String
        protected set

    var `object`: Any? = null
        protected set

    init {
        this.message = message
        this.`object` = `object`
    }
}