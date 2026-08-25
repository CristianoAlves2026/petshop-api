package crm.petshop.repository;

import crm.petshop.model.Raca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RacaRepository extends JpaRepository<Raca, Long> {

    List<Raca> findAllByOrderByNomeAsc();

    Optional<Raca> findByNome(String nome);
}