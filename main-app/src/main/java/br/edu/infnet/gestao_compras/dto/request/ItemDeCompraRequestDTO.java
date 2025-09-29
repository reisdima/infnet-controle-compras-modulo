package br.edu.infnet.gestao_compras.dto.request;

import java.math.BigDecimal;

public class ItemDeCompraRequestDTO {

    private String codidoDeBarras;
    private BigDecimal preco;
    private Integer quantidade;



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

    public String getCodidoDeBarras() {
        return codidoDeBarras;
    }

    public void setCodidoDeBarras(String codidoDeBarras) {
        this.codidoDeBarras = codidoDeBarras;
    }

}
