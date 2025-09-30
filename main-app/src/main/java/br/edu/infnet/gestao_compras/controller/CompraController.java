package br.edu.infnet.gestao_compras.controller;

import br.edu.infnet.gestao_compras.dto.request.CompraRequestDTO;
import br.edu.infnet.gestao_compras.dto.response.CompraResponseDTO;
import br.edu.infnet.gestao_compras.model.domain.Compra;
import br.edu.infnet.gestao_compras.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {


    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> obterLista() {
        List<CompraResponseDTO> lista = compraService.obterLista();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> obterPorId(@PathVariable Integer id) {
        CompraResponseDTO compra = new CompraResponseDTO(compraService.obterPorId(id));
        if (compra == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(compra);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        compraService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> cadastrarCompra(@Valid  @RequestBody CompraRequestDTO compra) {
        CompraResponseDTO novaCompra = compraService.incluir(compra);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaCompra);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CompraResponseDTO> alterar(@PathVariable Integer id, @RequestBody CompraRequestDTO compra) {
        CompraResponseDTO compraAlterada = compraService.alterar(id, compra);

        return ResponseEntity.ok(compraAlterada);
    }

}
