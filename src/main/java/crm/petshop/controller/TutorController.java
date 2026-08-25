package crm.petshop.controller;


import crm.petshop.dto.TutorDTO;
import crm.petshop.model.Tutor;
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


    // ✅ LOGIN — AGORA RETORNA id + nome
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String cpf = dados.get("cpf");
        String senha = dados.get("senha");

        boolean acessoPermitido = tutorService.fazerLogin(cpf, senha);

        if (!acessoPermitido) {
            return ResponseEntity.status(401).body("❌ CPF ou senha incorretos");
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        Optional<Tutor> tutor = tutorRepository.findByCpf(cpfLimpo);

        if (tutor.isPresent()) {
            // ✅ DEVOLVE id + nome
            return ResponseEntity.ok(Map.of(
                "mensagem", "✅ Acesso permitido",
                "id", tutor.get().getId(),          // ← ✅ ACRESCENTEI ESTA LINHA
                "nome", tutor.get().getNome()
            ));
        }

        return ResponseEntity.status(401).body("❌ Tutor não encontrado");
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


    // ✅ RECUPERAÇÃO DE SENHA
    @PostMapping("/recuperar-senha")
    public ResponseEntity<Map<String, String>> recuperarSenha(@RequestBody Map<String, String> dados) {
        String cpf = dados.get("cpf");
        String email = dados.get("email");

        Optional<Tutor> tutor = tutorRepository.findByCpfAndEmail(cpf, email);

        if (tutor.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                "erro", "CPF ou E-mail não encontrados"
            ));
        }

        String novaSenha = String.format("%06d", new Random().nextInt(1000000));
        Tutor t = tutor.get();
        t.setSenha(passwordEncoder.encode(novaSenha));
        tutorRepository.save(t);

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