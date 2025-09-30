package br.edu.infnet.gestao_compras.service;

import br.edu.infnet.gestao_compras.dto.request.ItemDeCompraRequestDTO;
import br.edu.infnet.gestao_compras.dto.response.ItemDeCompraResponseDTO;
import br.edu.infnet.gestao_compras.model.domain.Compra;
import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;
import br.edu.infnet.gestao_compras.model.domain.Produto;
import br.edu.infnet.gestao_compras.model.domain.exceptions.EntidadeInvalidaException;
import br.edu.infnet.gestao_compras.model.domain.exceptions.EntidadeNaoEncontradaException;
import br.edu.infnet.gestao_compras.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ProdutoService produtoService;
    private final CompraService compraService;

    public ItemService(ItemRepository itemRepository, ProdutoService produtoService,
                       CompraService compraService) {
        this.itemRepository = itemRepository;
        this.produtoService = produtoService;
        this.compraService = compraService;
    }


//    @Transactional
//    public ItemDeCompra incluir(ItemDeCompraRequestDTO dto) {
//        validarItem(dto);
//
//        Produto produto = produtoService.obterProdutoPorCodigoDeBarras(dto.getCodidoDeBarras());
//        Compra compra = compraService.obterPorNotaFiscal(dto.getNotaFiscal());
//
//        ItemDeCompra itemDeCompra = new ItemDeCompra();
//        itemDeCompra.setPreco(dto.getPreco());
//        itemDeCompra.setCompra(compra);
//        itemDeCompra.setProduto(produto);
//        itemDeCompra.setQuantidade(dto.getQuantidade());
//
//        return itemRepository.save(itemDeCompra);
//    }


//    @Transactional
//    public ItemDeCompra alterar(Integer id, ItemDeCompraRequestDTO dto) {
//        obterPorId(id);
//        validarItem(dto);
//
//        Produto produto = produtoService.obterProdutoPorCodigoDeBarras(dto.getCodidoDeBarras());
//        Compra compra = compraService.obterPorNotaFiscal(dto.getNotaFiscal());
//
//        ItemDeCompra itemDeCompra = new ItemDeCompra();
//        itemDeCompra.setPreco(dto.getPreco());
//        itemDeCompra.setCompra(compra);
//        itemDeCompra.setProduto(produto);
//        itemDeCompra.setQuantidade(dto.getQuantidade());
//
//        itemDeCompra.setId(id);
//        return itemRepository.save(itemDeCompra);
//    }


    public ItemDeCompra obterPorId(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID para alteração é inválido!");
        }
        return itemRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("O item com ID " + id + " não foi encontrado!"));
    }


//    @Transactional
//    public void excluir(Integer id) {
//        ItemDeCompra itemDeCompra =  itemRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("O item com ID " + id + " não foi encontrado!"));
//
//        itemRepository.delete(itemDeCompra);
//    }


//    public List<ItemDeCompraResponseDTO> obterLista() {
//        List<ItemDeCompra> itens =  itemRepository.findAll();
//        return itens.stream().map(item -> new ItemDeCompraResponseDTO(item)).collect(Collectors.toList());
//    }
//
//    public List<ItemDeCompra> obterItensPorProduto(Produto produto) {
//        return this.itemRepository.findByProduto(produto).get();
//    }
//
//    private void validarItem(ItemDeCompraRequestDTO item) {
//        if (item == null) {
//            throw new IllegalArgumentException("O item não pode estar nulo!");
//        }
//        if (item.getCodidoDeBarras() == null || item.getPreco() == null || item.getNotaFiscal() == null) {
//            throw new EntidadeInvalidaException("O codigo de barras, valor e nota fiscal do item podem estar vazios!");
//        }
//    }
}
