package crm.petshop.repository;

import crm.petshop.model.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    // ✅ BUSCAR TODOS OS LANÇAMENTOS DE UM PET — ORDENADOS POR DATA (MAIS RECENTE PRIMEIRO)
    List<Lancamento> findByIdPetOrderByDataDesc(Long idPet);
}