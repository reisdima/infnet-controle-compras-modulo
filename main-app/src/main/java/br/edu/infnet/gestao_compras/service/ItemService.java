package br.edu.infnet.gestao_compras.service;

import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;
import br.edu.infnet.gestao_compras.model.domain.Produto;
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

}
