package crm.petshop.controller;

import crm.petshop.dto.TutorDTO;
import crm.petshop.service.TutorService;
import crm.petshop.repository.CidadeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TutorController {

    private final TutorService tutorService;
    private final CidadeRepository cidadeRepository;

    // ✅ LOGIN — EXATAMENTE COMO O FLUTTER PEDE
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String cpf = dados.get("cpf");
        String senha = dados.get("senha");

        boolean valido = tutorService.fazerLogin(cpf, senha);
        
        if (valido) {
            return ResponseEntity.ok().build(); // ✅ 200 OK
        } else {
            return ResponseEntity.status(401).body("CPF ou senha incorretos"); // ❌ 401
        }
    }

    // ✅ CADASTRO
    @PostMapping("/tutores")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody TutorDTO dto) {
        try {
            tutorService.cadastrar(dto);
            return ResponseEntity.status(201).body("✅ Cadastro realizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
    // ✅ TRATAR ERROS DE VALIDAÇÃO
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarErroValidacao(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        StringBuilder mensagem = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(erro -> {
            mensagem.append(erro.getDefaultMessage()).append(" ");
        });
        return ResponseEntity.badRequest().body(mensagem.toString().trim());
    }
}