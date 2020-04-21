package com.code.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class Application {

    companion object {
        @JvmStatic
        open fun main(args: Array<String>) {
            runApplication<Application>(*args)
        }
    }
}