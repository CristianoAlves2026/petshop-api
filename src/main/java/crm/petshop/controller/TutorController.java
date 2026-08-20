package crm.petshop.controller;

import crm.petshop.dto.TutorDTO;
import crm.petshop.service.TutorService;
import crm.petshop.repository.CidadeRepository;
import crm.petshop.repository.TutorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TutorController {

    private final TutorService tutorService;
    private final CidadeRepository cidadeRepository;
    private final TutorRepository tutorRepository;
    private final PasswordEncoder passwordEncoder;

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

    // ✅ RECUPERAÇÃO DE SENHA — NOVA ROTA ADICIONADA
    @PostMapping("/recuperar-senha")
    public ResponseEntity<Map<String, String>> recuperarSenha(@RequestBody Map<String, String> dados) {
        String cpf = dados.get("cpf");
        String email = dados.get("email");

        Optional<crm.petshop.model.Tutor> tutor = tutorRepository.findByCpfAndEmail(cpf, email);

        if (tutor.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                "erro", "CPF ou E-mail não encontrados"
            ));
        }

        // ✅ Gerar senha de 6 dígitos aleatórios
        String novaSenha = String.format("%06d", new Random().nextInt(1000000));

        // ✅ Criptografar e salvar
        crm.petshop.model.Tutor t = tutor.get();
        t.setSenha(passwordEncoder.encode(novaSenha));
        tutorRepository.save(t);

        // ✅ Retorna nome e nova senha
        return ResponseEntity.ok(Map.of(
            "nome", t.getNome(),
            "novaSenha", novaSenha
        ));
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