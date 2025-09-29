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
import java.util.stream.Collectors;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProdutoService produtoService;

    public CompraService(CompraRepository compraRepository, ProdutoService produtoService) {
        this.compraRepository = compraRepository;
        this.produtoService = produtoService;
    }


    @Transactional
    public CompraResponseDTO incluir(CompraRequestDTO dto) {
        validarCompra(dto);
        Compra compra = new Compra();
        compra.setDataDaCompra(dto.getDataDaCompra());
        compra.setEstabelecimento(dto.getEstabelecimento());
        compra.setNotaFiscal(dto.getNotaFiscal());

        List<ItemDeCompra> listaDeItens = new ArrayList<>();

        for (ItemDeCompraRequestDTO item : dto.getProdutos()) {
            Produto produto = this.produtoService.obterProdutoPorCodigoDeBarras(item.getCodidoDeBarras());
            if (produto == null) {
                throw new EntidadeNaoEncontradaException("Produto com codigo de barras " + item.getCodidoDeBarras() + " não encontrado.");
            }
            var itemDeCompra = new ItemDeCompra();
            itemDeCompra.setProduto(produto);
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
        obterPorId(id);
        validarCompra(dto);

        Compra compraEncontrada = this.compraRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Compra com id " + id + " não encontrada."));

        if (!compraEncontrada.getNotaFiscal().equals(dto.getNotaFiscal())) {
            this.compraRepository.findByNotaFiscal(dto.getNotaFiscal())
                    .ifPresent(v -> {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma compra com essa nota fiscal");
                    });
            compraEncontrada.setNotaFiscal(dto.getNotaFiscal());
        }

        compraEncontrada.setDataDaCompra(dto.getDataDaCompra());
        compraEncontrada.setEstabelecimento(dto.getEstabelecimento());

        compraEncontrada.getItensDeCompra().clear();
        List<ItemDeCompra> listaDeItens = new ArrayList<>();

        for (ItemDeCompraRequestDTO item : dto.getProdutos()) {
            Produto produto = this.produtoService.obterProdutoPorCodigoDeBarras(item.getCodidoDeBarras());
            if (produto == null) {
                throw new EntidadeNaoEncontradaException("Produto com codigo de barras " + item.getCodidoDeBarras() + " não encontrado.");
            }
            var itemDeCompra = new ItemDeCompra();
            itemDeCompra.setProduto(produto);
            itemDeCompra.setCompra(compraEncontrada);
            itemDeCompra.setPreco(item.getPreco());
            itemDeCompra.setQuantidade(item.getQuantidade());

            listaDeItens.add(itemDeCompra);
        }
        compraEncontrada.setItensDeCompra(listaDeItens);


        return new CompraResponseDTO(compraRepository.save(compraEncontrada));
    }


    public CompraResponseDTO obterPorId(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID para alteração é inválido!");
        }
        Compra compra = compraRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("A compra com ID " + id + " não foi encontrada!"));
        CompraResponseDTO responseDto = new CompraResponseDTO(compra);
        return responseDto;
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
        if (compra.getProdutos() == null || compra.getProdutos().isEmpty()) {
            throw new IllegalArgumentException("A compra não pode ter lista de produtos vazia!");
        }
        if (compra.getDataDaCompra() == null) {
            compra.setDataDaCompra(LocalDate.now());
        }
    }
}
