package com.github.joshuatcasey.d4s

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.kie.api.runtime.KieContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert

@Service
class TokenTimeValidationService {

    private final val rulesLogger: Log = LogFactory.getLog("rules_logger")

    @Autowired
    private lateinit var kieContainer: KieContainer;

    fun validate(input: Input): Output {
        Assert.notNull(input, "input is required")
        Assert.notNull(input.time, "time is required")

        val kieSession = kieContainer.newStatelessKieSession("rulesSession")
        val wrapper = DroolsWrapper(input, Output())
        kieSession.setGlobal("rulesLogger", rulesLogger);
        kieSession.execute(wrapper)
        return wrapper.output
    }
}
