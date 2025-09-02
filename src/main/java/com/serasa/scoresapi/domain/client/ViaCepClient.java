package com.serasa.scoresapi.domain.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "${viacep.url}")
public interface ViaCepClient {
    
    @GetMapping("/{cep}/json")
    EnderecoResponse buscarCep(@PathVariable("cep") String cep);
}
