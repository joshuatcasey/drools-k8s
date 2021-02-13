package com.github.joshuatcasey.d4s

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import java.util.stream.Stream


@SpringBootTest
@AutoConfigureMockMvc
class DroolsTests {

    class RulesArgumentsProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments?> {
            val n = null
            return Stream.of(
                Arguments.of(Input(exp = n, iat = n, nbf = n, time = 1),
                    Output(valid = true, Collections.emptySet()),
                    "no restrictions"),
                Arguments.of(Input(exp = n, iat = 0, nbf = n, time = 1),
                    Output(valid = true, Collections.emptySet()),
                    "time == iat"),
                Arguments.of(Input(exp = n, iat = 0, nbf = n, time = 0),
                    Output(valid = true, Collections.emptySet()),
                    "time >= iat"),
                Arguments.of(Input(exp = n, iat = 1, nbf = n, time = 0),
                    Output(valid = false, Collections.singleton("iat > time")),
                    "iat > time"),
                Arguments.of(Input(exp = 3, iat = 0, nbf = 1, time = 2),
                    Output(valid = true, Collections.emptySet()),
                    "iat <= nbf <= time <= exp"),
            )
        }
    }

    @Nested
    inner class Rules {

        @Autowired
        lateinit var validationService: TokenTimeValidationService

        @Test
        internal fun requiresTime(@Autowired validationService: TokenTimeValidationService) {
            val input = Input(
                exp = null,
                iat = null,
                nbf = null,
                time = null,
            )

            assertThrows(IllegalArgumentException::class.java, {
                validationService.validate(input)
            }, "time is required")
        }

        @ParameterizedTest
        @ArgumentsSource(RulesArgumentsProvider::class)
        internal fun checkValidity(
            input: Input,
            expectedOutput: Output,
            description: String,
        ) {
            val actualOutput = validationService.validate(input)

            assertEquals(expectedOutput, actualOutput, description)
        }
    }

    @Nested
    inner class Endpoint {

        private val inputJson = """
        {
            "iat": 111,
            "nbf": 222,
            "time": 333,
            "exp": 444
        }
        """.trimIndent()

        @Test
        internal fun deserialize(@Autowired objectMapper: ObjectMapper) {
            val input: Input = objectMapper.readValue(inputJson)
            val expected = Input(
                exp = 444,
                iat = 111,
                nbf = 222,
                time = 333,
            )
            assertThat(input, `is`(expected))
        }

        @Test
        internal fun post(@Autowired mockMvc: MockMvc) {
            val post = post("/validate")
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON)

            mockMvc.perform(post)
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.valid").value("true"))
        }

    }

}
