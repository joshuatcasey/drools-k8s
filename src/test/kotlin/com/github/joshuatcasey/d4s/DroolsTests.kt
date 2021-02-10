package com.github.joshuatcasey.d4s

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
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

    val inputJson =
        """
        {
            "something": 900
        }
        """.trimIndent()

    @Test
    internal fun deserialize(@Autowired objectMapper: ObjectMapper) {
        val input: Input = objectMapper.readValue(inputJson)
        assertThat(input, `is`(Input(900)))
    }

    @Test
    internal fun post(@Autowired mockMvc: MockMvc) {
        val post = post("/validate")
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON)

        mockMvc.perform(post)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.something").value("1800"))
    }

    @Test
    internal fun simpleTest(@Autowired logicService: LogicService) {
        val input = Input(42)

        val output = logicService.applyLogic(input)

        assertThat(output, `is`(Output(84)))
    }
}
