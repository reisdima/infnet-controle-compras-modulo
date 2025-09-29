//package br.edu.infnet.gestao_compras.controller;
//
//import br.edu.infnet.caiovincenzo.model.domain.Item;
//import br.edu.infnet.caiovincenzo.model.service.ItemService;
//import br.edu.infnet.gestao_compras.dto.request.ItemDeCompraRequestDTO;
//import br.edu.infnet.gestao_compras.dto.response.ItemDeCompraResponseDTO;
//import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;
//import br.edu.infnet.gestao_compras.service.ItemService;
//import jakarta.validation.Valid;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/itemsAlimenticios")
//public class ItemController {
//
//
//    private final ItemService itemService;
//
//    public ItemController(ItemService itemService) {
//        this.itemService = itemService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ItemDeCompraResponseDTO>> obterLista() {
//        List<ItemDeCompraResponseDTO> lista = itemService.obterLista();
//        if (lista.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(lista);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ItemDeCompraResponseDTO> obterPorId(@PathVariable Integer id) {
//        ItemDeCompraResponseDTO item = itemService.obterPorId(id);
//        if (item == null) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(item);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
//        itemService.excluir(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping
//    public ResponseEntity<ItemDeCompraResponseDTO> incluir(@Valid  @RequestBody ItemDeCompraRequestDTO item) {
//        ItemDeCompra novoItem = itemService.incluir(item);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
//    }
//
//    @PutMapping(value = "/{id}")
//    public ResponseEntity<ItemDeCompraResponseDTO> alterar(@PathVariable Integer id, @RequestBody ItemDeCompraRequestDTO item) {
//        ItemDeCompra itemAlterado = itemService.alterar(id, item);
//
//        return ResponseEntity.ok(itemAlterado);
//    }
//
//
//}
