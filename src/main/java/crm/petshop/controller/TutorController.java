package crm.petshop.controller;


import crm.petshop.dto.TutorDTO;
import crm.petshop.model.Tutor;
import crm.petshop.service.NotificacaoService;
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
    private final NotificacaoService notificacaoService;


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

    // ✅ RECEBE E GUARDA O TOKEN DE NOTIFICAÇÃO
@PostMapping("/{id}/token")
public ResponseEntity<?> salvarToken(@PathVariable Long id, @RequestBody String token) {
    try {
        Optional<Tutor> tutor = tutorRepository.findById(id);
        if (tutor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // ✅ LIMPA AS ASPAS SE TIVER NO COMEÇO E NO FIM
        String tokenLimpo = token.trim();
        if (tokenLimpo.startsWith("\"") && tokenLimpo.endsWith("\"")) {
            tokenLimpo = tokenLimpo.substring(1, tokenLimpo.length() - 1);
        }
        
        tutor.get().setTokenFcm(tokenLimpo); // ✅ SALVA SEM ASPAS!
        tutorRepository.save(tutor.get());
        return ResponseEntity.ok("✅ Token salvo com sucesso!");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("❌ Erro: " + e.getMessage());
    }
}

        // ✅ TESTE — ENVIA NOTIFICAÇÃO PARA UM TUTOR
    @GetMapping("/{id}/notificar-teste")
    public ResponseEntity<?> notificarTeste(@PathVariable Long id) {
        try {
            // 1. Busca o tutor no banco
            Optional<Tutor> tutor = tutorRepository.findById(id);
            if (tutor.isEmpty() || tutor.get().getTokenFcm() == null) {
                return ResponseEntity.notFound().build();
            }

            // 2. Pega o serviço de notificação e ENVIA
            String resultado = notificacaoService.enviar(
                tutor.get().getTokenFcm(),
                "🔔 Teste de Notificação",
                "Olá! Notificação funcionando perfeitamente! 🎉"
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Erro: " + e.getMessage());
        }
    }
    
}