package br.edu.infnet.gestao_compras.controller;

import br.edu.infnet.gestao_compras.dto.request.ProdutoRequestDTO;
import br.edu.infnet.gestao_compras.dto.response.ProdutoResponseDTO;
import br.edu.infnet.gestao_compras.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> obterLista() {
        List<ProdutoResponseDTO> lista = produtoService.obterLista();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(@PathVariable("id") Integer id) {
        ProdutoResponseDTO produto = new ProdutoResponseDTO(produtoService.obterPorId(id));
        if (produto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/codigo/{codigoDeBarras}")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(@PathVariable("id") String codigoDeBarras) {
        ProdutoResponseDTO produto = this.produtoService.obterProdutoPorCodigoDeBarras(codigoDeBarras);
        if (produto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Integer id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> incluir(@Valid @RequestBody ProdutoRequestDTO produto) {
        ProdutoResponseDTO novoProduto = produtoService.incluir(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> alterar(@PathVariable("id") Integer id, @RequestBody ProdutoRequestDTO produto) {
        ProdutoResponseDTO produtoAlterado = produtoService.alterar(id, produto);

        return ResponseEntity.ok(produtoAlterado);
    }

}
