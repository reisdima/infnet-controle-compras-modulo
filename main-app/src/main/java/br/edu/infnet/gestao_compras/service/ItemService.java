package br.edu.infnet.gestao_compras.service;

import br.edu.infnet.gestao_compras.dto.request.ItemDeCompraRequestDTO;
import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;
import br.edu.infnet.gestao_compras.model.domain.Produto;
import br.edu.infnet.gestao_compras.model.domain.exceptions.EntidadeInvalidaException;
import br.edu.infnet.gestao_compras.model.domain.exceptions.EntidadeNaoEncontradaException;
import br.edu.infnet.gestao_compras.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public ItemDeCompra obterPorId(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID para alteração é inválido!");
        }
        return itemRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("O item com ID " + id + " não foi encontrado!"));
    }

    public List<ItemDeCompra> obterItensPorProduto(Produto produto) {
        return this.itemRepository.findByProduto(produto).get();
    }

    private void validarItem(ItemDeCompraRequestDTO item) {
        if (item == null) {
            throw new IllegalArgumentException("O item não pode estar nulo!");
        }
        if (item.getCodidoDeBarras() == null || item.getPreco() == null || item.getNotaFiscal() == null) {
            throw new EntidadeInvalidaException("O codigo de barras, valor e nota fiscal do item podem estar vazios!");
        }
    }
}
