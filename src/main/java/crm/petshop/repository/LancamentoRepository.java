package crm.petshop.repository;

import crm.petshop.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    // ✅ BUSCAR TODOS OS LANÇAMENTOS DE UM PET
    List<Lancamento> findByIdPetOrderByDataDesc(Long idPet);

    // ✅ BUSCA SÓ OS ATIVOS NO PERÍODO DE NOTIFICAÇÃO
    @Query("""
        SELECT l FROM Lancamento l
        WHERE l.status = 'ATIVO'
          AND l.repetir IS NOT NULL
          AND l.repetir <= :dataLimite
    """)
    List<Lancamento> buscarAtivosParaNotificar(
        @Param("dataLimite") LocalDate dataLimite
    );
}