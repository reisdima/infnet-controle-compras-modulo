package br.edu.infnet.gestao_compras.repository;

import br.edu.infnet.gestao_compras.model.domain.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    Optional<Compra> findByNotaFiscal(String notaFiscal);
}
