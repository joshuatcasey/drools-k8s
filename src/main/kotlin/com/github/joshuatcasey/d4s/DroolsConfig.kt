package com.github.joshuatcasey.d4s

import org.kie.api.KieServices
import org.kie.api.runtime.KieContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DroolsConfig {
    @Bean
    fun kieContainer(): KieContainer {
        return KieServices.Factory.get().newKieClasspathContainer()
    }
}
