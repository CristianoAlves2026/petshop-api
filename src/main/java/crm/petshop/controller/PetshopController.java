package crm.petshop.controller;

import crm.petshop.model.Petshop;
import crm.petshop.repository.PetshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/petshops")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PetshopController {

    private final PetshopRepository petshopRepository;

    // ✅ Lista TODOS em ORDEM ALFABÉTICA por Nome Fantasia
    @GetMapping
    public List<Petshop> listarTodos() {
        return petshopRepository.findAll().stream()
            .sorted((a, b) -> a.getNomeFantasia().compareToIgnoreCase(b.getNomeFantasia()))
            .toList();
    }

    // ✅ Busca por ID (o que o cliente digita como "código")
    @GetMapping("/{id}")
    public ResponseEntity<Petshop> buscarPorId(@PathVariable Long id) {
        return petshopRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}