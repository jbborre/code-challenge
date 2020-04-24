package com.code.challenge

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

private val LOGGER = KotlinLogging.logger {}

@SpringBootApplication
class Application {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Application>(*args)
        }
    }
}