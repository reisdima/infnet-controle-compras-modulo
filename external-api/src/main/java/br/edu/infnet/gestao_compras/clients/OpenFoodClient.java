package br.edu.infnet.gestao_compras.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "openfood-products", url = "${api.openfood.url}")
public interface OpenFoodClient {

    @GetMapping("/product/{codigoDeBarras}.json")
    Object obterProduto(@PathVariable String codigoDeBarras);
}
