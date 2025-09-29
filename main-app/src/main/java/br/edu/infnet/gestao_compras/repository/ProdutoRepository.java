package br.edu.infnet.gestao_compras.repository;

import br.edu.infnet.gestao_compras.model.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    Optional<Produto> findByCodigoDeBarras(String codigoDeBarras);
}
