package crm.petshop.controller;

import crm.petshop.dto.PetDTO;
import crm.petshop.model.Pet;
import crm.petshop.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    // ✅ CADASTRAR PET
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody PetDTO dto) {
        try {
            Pet pet = petService.cadastrar(dto);
            return ResponseEntity.status(201).body(Map.of(
                "mensagem", "✅ Pet cadastrado com sucesso!",
                "id", pet.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "erro", "❌ Erro ao cadastrar: " + e.getMessage()
            ));
        }
    }

    // ✅ ENVIAR FOTO
    @PostMapping("/foto")
    public ResponseEntity<?> uploadFoto(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            String urlFoto = petService.enviarFoto(arquivo);
            return ResponseEntity.ok(Map.of("urlFoto", urlFoto));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "erro", "❌ Erro ao enviar foto: " + e.getMessage()
            ));
        }
    }

    // ✅ LISTAR PETS DO TUTOR
    @GetMapping("/tutor/{idTutor}")
    public List<Pet> listarPorTutor(@PathVariable Long idTutor) {
        return petService.listarPorTutor(idTutor);
    }

        // ✅ ATUALIZAR PET
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody PetDTO dto) {
        try {
            Pet pet = petService.atualizar(id, dto);
            return ResponseEntity.ok(Map.of(
                "mensagem", "✅ Pet atualizado com sucesso!",
                "id", pet.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "erro", "❌ Erro ao atualizar: " + e.getMessage()
            ));
        }
    }

    // ✅ EXCLUIR PET
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            petService.excluir(id);
            return ResponseEntity.ok(Map.of(
                "mensagem", "✅ Pet excluído com sucesso!"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "erro", "❌ Erro ao excluir: " + e.getMessage()
            ));
        }
    }




}