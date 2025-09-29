package br.edu.infnet.gestao_compras.service;

import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;
import br.edu.infnet.gestao_compras.model.domain.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoAnalyticsService {
    private final ProdutoService produtoService;
    private final ItemService itemService;

    public ProdutoAnalyticsService(ProdutoService produtoService, ItemService itemService) {
        this.itemService = itemService;
        this.produtoService = produtoService;
    }

    public BigDecimal calcularMediaDeProduto(Integer produtoId) {
        Produto produto = produtoService.obterPorId(produtoId);
        List<ItemDeCompra> itens = itemService.obterItensPorProduto(produto);
        if(itens == null || itens.size() == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal soma = BigDecimal.ZERO;
        for(ItemDeCompra item : itens) {
            soma = soma.add(item.getPreco());
        }
        return soma.divide(new BigDecimal(itens.size()));
    }


}
