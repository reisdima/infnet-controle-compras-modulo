package br.edu.infnet.gestao_compras.dto.response;

import br.edu.infnet.gestao_compras.dto.request.ItemDeCompraRequestDTO;
import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;

import java.math.BigDecimal;

public class ItemDeCompraResponseDTO {

    private ProdutoResponseDTO produto;
    private BigDecimal preco;
    private Integer quantidade;
    private String notaFiscal;

    public ItemDeCompraResponseDTO(ItemDeCompra itemDeCompra) {
        ProdutoResponseDTO produtoResponseDTO = new ProdutoResponseDTO(itemDeCompra.getProduto());
        this.produto = produtoResponseDTO;
        this.preco = itemDeCompra.getPreco();
        this.quantidade = itemDeCompra.getQuantidade();
        this.notaFiscal = itemDeCompra.getCompra().getNotaFiscal();
    }



    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public ProdutoResponseDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoResponseDTO produto) {
        this.produto = produto;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}
