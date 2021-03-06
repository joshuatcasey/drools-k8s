package com.github.joshuatcasey.d4s

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    val tokenTimeValidationService: TokenTimeValidationService,
) {
    @PostMapping("/validate")
    fun greeting(@RequestBody input: Input) = tokenTimeValidationService.validate(input)
}
