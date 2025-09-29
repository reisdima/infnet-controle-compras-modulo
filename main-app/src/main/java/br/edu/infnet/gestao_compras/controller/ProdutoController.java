package br.edu.infnet.gestao_compras.controller;

import br.edu.infnet.gestao_compras.model.domain.Produto;
import br.edu.infnet.gestao_compras.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> obterLista() {
        List<Produto> lista = produtoService.obterLista();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterPorId(@PathVariable Integer id) {
        Produto produto = produtoService.obterPorId(id);
        if (produto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/codigo/{codigoDeBarras}")
    public ResponseEntity<Produto> obterPorId(@PathVariable String codigoDeBarras) {
        Produto produto = this.produtoService.obterProdutoPorCodigoDeBarras(codigoDeBarras);
        if (produto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produto);
    }



    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Produto> incluir(@Valid @RequestBody Produto produto) {
        Produto novoProduto = produtoService.incluir(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Produto> alterar(@PathVariable Integer id, @RequestBody Produto produto) {
        Produto produtoAlterado = produtoService.alterar(id, produto);

        return ResponseEntity.ok(produtoAlterado);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Produto> alterarDiasValidade(@PathVariable Integer id, @RequestBody Integer diasValidade) {
        Produto produtoAlterado = produtoService.trocarDataValidade(id, diasValidade);

        return ResponseEntity.ok(produtoAlterado);
    }
}
