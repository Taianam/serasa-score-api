package com.serasa.scoresapi.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class ViaCepTestConfiguration {
    
    // Esta configuração está vazia agora
    // Os mocks são configurados diretamente nos testes usando @MockBean
}