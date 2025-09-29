package br.edu.infnet.gestao_compras.dto.request;

import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraRequestDTO {

    private LocalDate dataDaCompra;
    private List<ItemDeCompraRequestDTO> produtos = new ArrayList<>();
    private String estabelecimento;
    private String notaFiscal;

    public LocalDate getDataDaCompra() {
        return dataDaCompra;
    }

    public void setDataDaCompra(LocalDate dataDaCompra) {
        this.dataDaCompra = dataDaCompra;
    }

    public List<ItemDeCompraRequestDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemDeCompraRequestDTO> produtos) {
        this.produtos = produtos;
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}
