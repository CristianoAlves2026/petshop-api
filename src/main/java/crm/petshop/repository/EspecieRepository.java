package crm.petshop.repository;

import crm.petshop.model.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecieRepository extends JpaRepository<Especie, Long> {
    
    List<Especie> findAllByOrderByNomeEspecieAsc();
}