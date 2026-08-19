package crm.petshop.service;

import crm.petshop.dto.TutorDTO;
import crm.petshop.model.Tutor;
import crm.petshop.repository.CidadeRepository;
import crm.petshop.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TutorService {

    private final TutorRepository tutorRepository;
    private final CidadeRepository cidadeRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ CADASTRO com todas as validações
    public Tutor cadastrar(TutorDTO dto) {
        String cpfSemPontos = dto.getCpf().replaceAll("[^0-9]", "");
        dto.setCpf(cpfSemPontos);

        if (!validarCPF(cpfSemPontos)) {
            throw new RuntimeException("❌ CPF inválido! Verifique os dígitos.");
        }

        String telefoneLimpo = dto.getTelefone().replaceAll("[^0-9]", "");
        if (telefoneLimpo.length() != 11) {
            throw new RuntimeException("❌ Telefone precisa ter 11 dígitos com DDD.");
        }
        if (telefoneLimpo.charAt(2) != '9') {
            throw new RuntimeException("❌ Telefone deve começar com dígito 9.");
        }
        if (tutorRepository.existsByTelefone(telefoneLimpo)) {
            throw new RuntimeException("❌ Este telefone já está cadastrado!");
        }

        if (tutorRepository.findByCpf(cpfSemPontos).isPresent()) {
            throw new RuntimeException("❌ Este CPF já está cadastrado!");
        }
        if (tutorRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("❌ Este e-mail já está cadastrado!");
        }

        if (dto.getIdMunicipio() == null || !cidadeRepository.existsById(dto.getIdMunicipio())) {
            throw new RuntimeException("❌ Município inválido! Selecione uma cidade da lista.");
        }

        var cidade = cidadeRepository.findById(dto.getIdMunicipio()).get();
        if (!cidade.getEstado().equalsIgnoreCase(dto.getEstado())) {
            throw new RuntimeException("❌ Estado não corresponde à cidade selecionada!");
        }

        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        Tutor tutor = new Tutor();
        tutor.setNome(dto.getNome());
        tutor.setCpf(cpfSemPontos);
        tutor.setTelefone(telefoneLimpo);
        tutor.setEmail(dto.getEmail());
        tutor.setSenha(senhaCriptografada);
        tutor.setIdPetshop(dto.getIdPetshop());
        tutor.setIdMunicipio(dto.getIdMunicipio());
        tutor.setBairro(dto.getBairro());
        tutor.setEstado(dto.getEstado().toUpperCase());

        return tutorRepository.save(tutor);
    }

    // ✅ LOGIN — EXATAMENTE COMO O FLUTTER PEDE
    public boolean fazerLogin(String cpf, String senha) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        Optional<Tutor> tutor = tutorRepository.findByCpf(cpfLimpo);
        
        if (tutor.isEmpty()) {
            return false;
        }
        return passwordEncoder.matches(senha, tutor.get().getSenha());
    }

    // ✅ VALIDA CPF
    private boolean validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return false;
        if (cpf.chars().allMatch(c -> c == cpf.charAt(0))) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        digito1 = digito1 >= 10 ? 0 : digito1;
        if (digito1 != (cpf.charAt(9) - '0')) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        digito2 = digito2 >= 10 ? 0 : digito2;
        if (digito2 != (cpf.charAt(10) - '0')) return false;

        return true;
    }
}