package crm.petshop.controller;

import crm.petshop.dto.LancamentoDTO;
import crm.petshop.model.Lancamento;
import crm.petshop.service.LancamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lancamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LancamentoController {

    private final LancamentoService lancamentoService;

    // ✅ SALVAR LANÇAMENTO
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody LancamentoDTO dto) {
        try {
            Lancamento lancamento = lancamentoService.cadastrar(dto);
            return ResponseEntity.ok(lancamento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar: " + e.getMessage());
        }
    }

    // ✅ LISTAR LANÇAMENTOS POR PET
    @GetMapping("/pet/{idPet}")
    public ResponseEntity<List<Lancamento>> listarPorPet(@PathVariable Long idPet) {
        List<Lancamento> lista = lancamentoService.listarPorPet(idPet);
        return ResponseEntity.ok(lista);
    }

    // ✅ IGNORAR LANÇAMENTO → MUDA STATUS PARA IGNORADO
    @PutMapping("/{id}/ignorar")
    public ResponseEntity<?> ignorar(@PathVariable Long id) {
        try {
            Lancamento lancamento = lancamentoService.buscarPorId(id); // ✅ RETORNA DIRETO, SEM Optional
            lancamento.setStatus("IGNORADO");
            lancamentoService.salvarEntidade(lancamento); // ✅ NOVO MÉTODO
            return ResponseEntity.ok().body(Map.of("mensagem", "Ignorado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ✅ EDITAR LANÇAMENTO
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @Valid @RequestBody LancamentoDTO dto) {
        try {
            Lancamento lancamento = lancamentoService.atualizar(id, dto);
            return ResponseEntity.ok(lancamento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ✅ EXCLUIR LANÇAMENTO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            lancamentoService.excluir(id);
            return ResponseEntity.ok(Map.of("mensagem", "Excluído com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ✅ ENVIAR FOTO — IGUAL AO DE PET!
    @PostMapping("/foto")
    public ResponseEntity<?> uploadFoto(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            String urlFoto = lancamentoService.enviarFoto(arquivo);
            return ResponseEntity.ok(Map.of("urlFoto", urlFoto));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }
}