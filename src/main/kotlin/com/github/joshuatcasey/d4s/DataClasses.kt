package com.github.joshuatcasey.d4s

data class Input(
    val exp: Int?,
    val iat: Int?,
    val nbf: Int?,
    val time: Int?,
)

data class Output(
    var valid: Boolean = false,
)

data class DroolsWrapper(
    val input: Input,
    var output: Output,
)

