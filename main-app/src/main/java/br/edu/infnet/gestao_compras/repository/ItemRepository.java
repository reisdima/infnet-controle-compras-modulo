package br.edu.infnet.gestao_compras.repository;

import br.edu.infnet.gestao_compras.model.domain.ItemDeCompra;
import br.edu.infnet.gestao_compras.model.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemDeCompra, Integer> {

    Optional<List<ItemDeCompra>> findByProduto(Produto produto);
}
