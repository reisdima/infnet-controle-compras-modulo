package br.edu.infnet.gestao_compras.dto.response;

import br.edu.infnet.gestao_compras.model.domain.Compra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompraResponseDTO {

    private LocalDate dataDaCompra;
    private List<ItemDeCompraResponseDTO> produtos = new ArrayList<>();
    private String estabelecimento;
    private String notaFiscal;

    public CompraResponseDTO (Compra compra) {
        this.dataDaCompra = compra.getDataDaCompra();
        this.estabelecimento = compra.getEstabelecimento();
        this.notaFiscal = compra.getNotaFiscal();
        this.produtos = compra.getItensDeCompra().stream()
                .map(ItemDeCompraResponseDTO::new).collect(Collectors.toList());
    }

    public LocalDate getDataDaCompra() {
        return dataDaCompra;
    }

    public void setDataDaCompra(LocalDate dataDaCompra) {
        this.dataDaCompra = dataDaCompra;
    }

    public List<ItemDeCompraResponseDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemDeCompraResponseDTO> produtos) {
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
