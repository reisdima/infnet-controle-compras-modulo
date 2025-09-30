package br.edu.infnet.gestao_compras.service;

import br.edu.infnet.gestao_compras.clients.OpenFoodClient;
import br.edu.infnet.gestao_compras.dto.request.ProdutoRequestDTO;
import br.edu.infnet.gestao_compras.dto.response.CompraResponseDTO;
import br.edu.infnet.gestao_compras.dto.response.ProdutoResponseDTO;
import br.edu.infnet.gestao_compras.model.domain.Compra;
import br.edu.infnet.gestao_compras.model.domain.OpenFoodProductResponse;
import br.edu.infnet.gestao_compras.model.domain.Produto;
import br.edu.infnet.gestao_compras.model.domain.exceptions.EntidadeInvalidaException;
import br.edu.infnet.gestao_compras.model.domain.exceptions.EntidadeNaoEncontradaException;
import br.edu.infnet.gestao_compras.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final OpenFoodClient openFoodClient;

    public ProdutoService(ProdutoRepository produtoRepository, OpenFoodClient openFoodClient) {
        this.produtoRepository = produtoRepository;
        this.openFoodClient = openFoodClient;
    }

    @Transactional
    public ProdutoResponseDTO obterProdutoPorCodigoDeBarras(String codigoDeBarras) {
        if (codigoDeBarras == null || codigoDeBarras.isEmpty()) {
            throw new IllegalArgumentException("Codigo de barras informado é inválido");
        }

        Produto produto = new Produto();
        System.out.println("Procurando no banco...");
        Optional<Produto> produtoDoBanco = this.obterProdutoEntityPorCodigoDeBarras(codigoDeBarras);
        if (produtoDoBanco.isPresent()) {
            System.out.println("Achou no banco");
            produto = produtoDoBanco.get();
        } else {
            System.out.println("Buscando no client...");
            OpenFoodProductResponse clientResponse = this.openFoodClient.obterProduto(codigoDeBarras);
            produto.setCodigoDeBarras(codigoDeBarras);
            produto.setMarca(clientResponse.getMarca());
            produto.setNome(clientResponse.getNome());
            produto.setUnidade(clientResponse.getUnidade());
            produto.setQuantidade(clientResponse.getQuantidade());
//            produto.setId(UUID.randomUUID());

            this.produtoRepository.save(produto);

        }

        return new ProdutoResponseDTO(produto);

    }

    protected Optional<Produto> obterProdutoEntityPorCodigoDeBarras(String codigoDeBarras) {
        Optional<Produto> produto = this.produtoRepository.findByCodigoDeBarras(codigoDeBarras);
        return produto;
    }

    public ProdutoResponseDTO incluir(ProdutoRequestDTO dto) {
        validarProduto(dto);
        if (this.produtoRepository.findByCodigoDeBarras(dto.getCodigoDeBarras()).isPresent()) {
            throw new IllegalArgumentException("Este produto, de código de barras " + dto.getCodigoDeBarras() + " já existe");
        }

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setMarca(dto.getMarca());
        produto.setUnidade(dto.getUnidade());
        produto.setCodigoDeBarras(dto.getCodigoDeBarras());
        produto.setQuantidade(dto.getQuantidade());

        return new ProdutoResponseDTO(this.produtoRepository.save(produto));
    }

    public ProdutoResponseDTO alterar(Integer id, ProdutoRequestDTO dto) {
        obterPorId(id);
        validarProduto(dto);

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setMarca(dto.getMarca());
        produto.setUnidade(dto.getUnidade());
        produto.setCodigoDeBarras(dto.getCodigoDeBarras());
        produto.setQuantidade(dto.getQuantidade());
        produto.setId(id);

        return new ProdutoResponseDTO(this.produtoRepository.save(produto));
    }

    public ProdutoResponseDTO obterPorId(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID para alteração é inválido!");
        }
        Produto produto =  produtoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("O produto com ID " + id + " não foi encontrado!"));
        return new ProdutoResponseDTO(produto);
    }

    public List<ProdutoResponseDTO> obterLista() {
        List<Produto> compras = produtoRepository.findAll();
        return compras.stream().map(compra -> {
            ProdutoResponseDTO responseDto = new ProdutoResponseDTO(compra);
            return responseDto;
        }).collect(Collectors.toList());
    }


    private void validarProduto(ProdutoRequestDTO produto) {
        if (produto == null) {
            throw new IllegalArgumentException("O produto não pode estar nulo!");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new EntidadeInvalidaException("O nome do produto não pode estar vazio!");
        }
    }
}
