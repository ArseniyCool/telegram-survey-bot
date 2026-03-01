package com.blacksun

import io.micronaut.runtime.Micronaut

object ApplicationKt {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("com.blacksun")
            .mainClass(ApplicationKt::class.java)
            .start()
    }
}