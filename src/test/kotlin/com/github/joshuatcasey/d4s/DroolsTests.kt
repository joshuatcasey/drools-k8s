package com.github.joshuatcasey.d4s

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class DroolsTests {

    @Nested
    inner class Rules {

        @Test
        internal fun validWithNoConstraints(@Autowired validationService: TokenTimeValidationService) {
            val input = Input(
                exp = null,
                iat = null,
                nbf = null,
                time = 1,
            )

            val output = validationService.validate(input)

            assertTrue(output.valid)
        }
    }

    @Nested
    inner class Endpoint {

        private val inputJson =
        """
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
