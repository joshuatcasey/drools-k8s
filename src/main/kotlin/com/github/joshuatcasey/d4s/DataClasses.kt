package com.github.joshuatcasey.d4s

import java.util.HashSet

data class Input(
    val exp: Int?,
    val iat: Int?,
    val nbf: Int?,
    val time: Int?,
)

data class Output(
    var valid: Boolean = false,
    var errors: Set<String> = HashSet(),
)

data class DroolsWrapper(
    val input: Input,
    var output: Output,
)

