package com.github.joshuatcasey.d4s

data class Input(
    val something: Int,
)

data class Output(
    val something: Int,
)

data class DroolsWrapper(
    val input: Input,
    var output: Output?,
)

