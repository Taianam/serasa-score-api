package com.serasa.scoresapi.facade;

import com.serasa.scoresapi.domain.client.EnderecoResponse;
import com.serasa.scoresapi.domain.client.ViaCepClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnderecoFacade {
    
    private final ViaCepClient viaCepClient;
    
    @Value("${validation.cep.length}")
    private int cepLength;
    
    public EnderecoFacade(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }
    
    public EnderecoResponse buscarEnderecoPorCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }
        

        String cepLimpo = cep.replaceAll("[^0-9]", "");
        
        if (cepLimpo.length() != cepLength) {
            throw new IllegalArgumentException("CEP deve ter " + cepLength + " dígitos");
        }
        
        try {
            return viaCepClient.buscarCep(cepLimpo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar CEP: " + cep, e);
        }
    }
    
    public boolean isValidCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            return false;
        }
        
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        return cepLimpo.length() == cepLength;
    }
}
