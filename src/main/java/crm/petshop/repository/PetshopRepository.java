package crm.petshop.repository;

import crm.petshop.model.Petshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetshopRepository extends JpaRepository<Petshop, Long> {
    // ✅ Já funciona por padrão: findById(id)
}