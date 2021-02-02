package com.github.joshuatcasey.d4s

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class D4sApplication

fun main(args: Array<String>) {
	runApplication<D4sApplication>(*args)
}
