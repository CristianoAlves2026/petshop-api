package crm.petshop.repository;

import crm.petshop.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByCpf(String cpf);
    Optional<Tutor> findByEmail(String email);
    
    // ✅ ÚNICA ADIÇÃO — busca por CPF + E-mail juntos
    Optional<Tutor> findByCpfAndEmail(String cpf, String email);
    
    boolean existsByCpf(String cpf);
    boolean existsByTelefone(String telefone);
}