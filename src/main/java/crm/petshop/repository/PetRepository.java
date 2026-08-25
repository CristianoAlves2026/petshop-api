package crm.petshop.repository;

import crm.petshop.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // ✅ Lista todos os Pets de um determinado Tutor
    List<Pet> findByIdTutorOrderByNomeAsc(Long idTutor);
}