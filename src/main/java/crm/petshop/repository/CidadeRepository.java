package crm.petshop.repository;

import crm.petshop.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    @Query("SELECT DISTINCT c.estado FROM Cidade c WHERE c.estado IS NOT NULL ORDER BY c.estado")
    List<String> buscarTodosEstados();

    @Query("SELECT c FROM Cidade c WHERE UPPER(c.estado) = UPPER(:estado) ORDER BY c.nome")
    List<Cidade> buscarPorEstado(@Param("estado") String estado);
}