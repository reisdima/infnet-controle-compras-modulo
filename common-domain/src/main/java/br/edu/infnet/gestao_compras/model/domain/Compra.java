package br.edu.infnet.gestao_compras.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate dataDaCompra;

    @NotBlank(message = "A nota fiscal é obrigatória")
    private String notaFiscal;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemDeCompra> produtos = new ArrayList<>();

    private String estabelecimento;

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", dataDaCompra=" + dataDaCompra +
                ", produtos=" + produtos +
                ", estabelecimento='" + estabelecimento + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataDaCompra() {
        return dataDaCompra;
    }

    public void setDataDaCompra(LocalDate dataDaCompra) {
        this.dataDaCompra = dataDaCompra;
    }

    public List<ItemDeCompra> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemDeCompra> produtos) {
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

    public BigDecimal getValor() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemDeCompra itemDeCompra : produtos) {
            BigDecimal subtotal = itemDeCompra.getPreco()
                    .multiply(BigDecimal.valueOf(itemDeCompra.getQuantidade()));
            total = total.add(subtotal);
        }
        return total;
    }
}
