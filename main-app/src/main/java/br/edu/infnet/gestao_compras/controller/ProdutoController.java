package br.edu.infnet.gestao_compras.controller;

import br.edu.infnet.gestao_compras.dto.request.ProdutoRequestDTO;
import br.edu.infnet.gestao_compras.dto.response.ProdutoResponseDTO;
import br.edu.infnet.gestao_compras.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ProdutoResponseDTO>> obterLista() {
        List<ProdutoResponseDTO> lista = produtoService.obterLista();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(@PathVariable("id") Integer id) {
        var produto = new ProdutoResponseDTO(produtoService.obterPorId(id));
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/codigo/{codigoDeBarras}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(@PathVariable("id") String codigoDeBarras) {
        var produto = this.produtoService.obterProdutoPorCodigoDeBarras(codigoDeBarras);
        if (produto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new ProdutoResponseDTO(produto));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable("id") Integer id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> incluir(@Valid @RequestBody ProdutoRequestDTO produto) {
        ProdutoResponseDTO novoProduto = produtoService.incluir(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> alterar(@PathVariable("id") Integer id, @RequestBody ProdutoRequestDTO produto) {
        ProdutoResponseDTO produtoAlterado = produtoService.alterar(id, produto);

        return ResponseEntity.ok(produtoAlterado);
    }

}
