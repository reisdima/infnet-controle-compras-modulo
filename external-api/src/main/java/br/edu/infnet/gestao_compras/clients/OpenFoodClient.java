package br.edu.infnet.gestao_compras.clients;

import br.edu.infnet.gestao_compras.model.domain.OpenFoodProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "openfood-products", url = "${api.openfood.url}")
public interface OpenFoodClient {

    @GetMapping("/product/{codigoDeBarras}.json")
    OpenFoodProductResponse obterProduto(@PathVariable("codigoDeBarras") String codigoDeBarras);
}
