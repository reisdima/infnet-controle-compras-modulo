package br.edu.infnet.gestao_compras.dto.response;

import br.edu.infnet.gestao_compras.model.domain.Compra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompraResponseDTO {

    private Integer id;
    private LocalDate dataDaCompra;
    private List<ItemDeCompraResponseDTO> itens = new ArrayList<>();
    private String estabelecimento;
    private String notaFiscal;

    public CompraResponseDTO (Compra compra) {
        this.dataDaCompra = compra.getDataDaCompra();
        this.id = compra.getId();
        this.estabelecimento = compra.getEstabelecimento();
        this.notaFiscal = compra.getNotaFiscal();
        this.itens = compra.getItensDeCompra().stream()
                .map(ItemDeCompraResponseDTO::new).collect(Collectors.toList());
    }

    public LocalDate getDataDaCompra() {
        return dataDaCompra;
    }

    public void setDataDaCompra(LocalDate dataDaCompra) {
        this.dataDaCompra = dataDaCompra;
    }

    public List<ItemDeCompraResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemDeCompraResponseDTO> itens) {
        this.itens = itens;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
