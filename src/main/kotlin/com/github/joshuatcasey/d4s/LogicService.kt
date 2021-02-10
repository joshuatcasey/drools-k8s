package com.github.joshuatcasey.d4s

import org.kie.api.runtime.KieContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LogicService {

    @Autowired
    private lateinit var kieContainer: KieContainer;

    fun applyLogic(input: Input): Output {
        val kieSession = kieContainer.newStatelessKieSession("rulesSession")
        val wrapper = DroolsWrapper(input, Output())
        kieSession.execute(wrapper)
        return wrapper.output
    }
}
