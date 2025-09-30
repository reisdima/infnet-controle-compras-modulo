package br.edu.infnet.gestao_compras.service;

import br.edu.infnet.gestao_compras.dto.request.CompraRequestDTO;
import br.edu.infnet.gestao_compras.dto.request.ItemDeCompraRequestDTO;
import br.edu.infnet.gestao_compras.dto.response.CompraResponseDTO;
import br.edu.infnet.gestao_compras.model.domain.Compra;
import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;
import br.edu.infnet.gestao_compras.model.domain.Produto;
import br.edu.infnet.gestao_compras.model.domain.exceptions.EntidadeNaoEncontradaException;
import br.edu.infnet.gestao_compras.repository.CompraRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;
    private final ItemService itemService;

    public CompraService(CompraRepository compraRepository, ProdutoService produtoService,
                         ItemService itemService) {
        this.compraRepository = compraRepository;
        this.produtoService = produtoService;
        this.itemService = itemService;
    }


    @Transactional
    public CompraResponseDTO incluir(CompraRequestDTO dto) {
        validarCompra(dto);
        Compra compra = new Compra();
        compra.setDataDaCompra(dto.getDataDaCompra());
        compra.setEstabelecimento(dto.getEstabelecimento());
        compra.setNotaFiscal(dto.getNotaFiscal());

        List<ItemDeCompra> listaDeItens = new ArrayList<>();

        for (ItemDeCompraRequestDTO item : dto.getItens()) {
            Optional<Produto> produto = this.produtoService.obterProdutoEntityPorCodigoDeBarras(item.getCodidoDeBarras());
            if (produto.isEmpty()) {
                throw new EntidadeNaoEncontradaException("Produto com codigo de barras " + item.getCodidoDeBarras() + " não encontrado.");
            }
            var itemDeCompra = new ItemDeCompra();
            itemDeCompra.setProduto(produto.get());
            itemDeCompra.setCompra(compra);
            itemDeCompra.setPreco(item.getPreco());
            itemDeCompra.setQuantidade(item.getQuantidade());

            listaDeItens.add(itemDeCompra);
        }
        compra.setItensDeCompra(listaDeItens);

        compra = compraRepository.save(compra);

        return new CompraResponseDTO(compra);

    }


    @Transactional
    public CompraResponseDTO alterar(Integer id, CompraRequestDTO dto) {

        validarCompra(dto);

        var compraEncontrada = obterPorId(id);

        if (!compraEncontrada.getNotaFiscal().equals(dto.getNotaFiscal())) {
            this.compraRepository.findByNotaFiscal(dto.getNotaFiscal())
                    .ifPresent(v -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma compra com essa nota fiscal");
                    });
            compraEncontrada.setNotaFiscal(dto.getNotaFiscal());
        }

        // Atualiza itens da compra
        if (dto.getItens() != null) {
            List<ItemDeCompra> itensAtualizados = new ArrayList<>();
            for (ItemDeCompraRequestDTO itemDto : dto.getItens()) {
                ItemDeCompra itemDeCompra;
                if (itemDto.getId() != null) {
                    itemDeCompra = new ItemDeCompra();
                    itemDeCompra.setId(itemDto.getId());
                } else {
                    itemDeCompra = this.itemService.obterPorId(itemDto.getId());
                }
                itemDeCompra.setCompra(compraEncontrada);
                itemDeCompra.setPreco(itemDto.getPreco());
                itemDeCompra.setQuantidade(itemDto.getQuantidade());
                itensAtualizados.add(itemDeCompra);
            }
            compraEncontrada.getItensDeCompra().clear();
            compraEncontrada.getItensDeCompra().addAll(itensAtualizados);
        }
        compraEncontrada.setDataDaCompra(dto.getDataDaCompra());
        compraEncontrada.setEstabelecimento(dto.getEstabelecimento());

        return new CompraResponseDTO(compraRepository.save(compraEncontrada));
    }


    public Compra obterPorId(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID informado é inválido!");
        }
        return compraRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("A compra com ID " + id + " não foi encontrada!"));
    }


    @Transactional
    public void excluir(Integer id) {
        Compra compra = compraRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("A compra com ID " + id + " não foi encontrada!"));

        compraRepository.delete(compra);
    }


    public List<CompraResponseDTO> obterLista() {
        List<Compra> compras = compraRepository.findAll();
        return compras.stream().map(compra -> {
            CompraResponseDTO responseDto = new CompraResponseDTO(compra);
            return responseDto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Compra obterPorNotaFiscal(String notaFiscal) {
        if (notaFiscal == null || notaFiscal.isEmpty()) {
            throw new IllegalArgumentException("Nota Fiscal informada é inválida");
        }
        return compraRepository.findByNotaFiscal(notaFiscal).orElseThrow(() -> new EntidadeNaoEncontradaException("A compra de nota fiscal " + notaFiscal + " não foi encontrado!"));
    }

    private void validarCompra(CompraRequestDTO compra) {
        if (compra == null) {
            throw new IllegalArgumentException("O compra não pode estar nulo!");
        }
        if (compra.getItens() == null || compra.getItens().isEmpty()) {
            throw new IllegalArgumentException("A compra não pode ter lista de produtos vazia!");
        }
        if (compra.getDataDaCompra() == null) {
            compra.setDataDaCompra(LocalDate.now());
        }
    }
}
