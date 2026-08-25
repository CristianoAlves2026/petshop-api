package crm.petshop.controller;

import crm.petshop.model.Raca;
import crm.petshop.repository.RacaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RacaController {

    private final RacaRepository racaRepository;

    // ✅ LISTAR TODAS AS RAÇAS — ORDENADAS POR NOME
    @GetMapping("/racas")
    public List<Raca> listarTodas() {
        return racaRepository.findAllByOrderByNomeAsc();
    }
}