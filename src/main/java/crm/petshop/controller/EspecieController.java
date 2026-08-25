package crm.petshop.controller;

import crm.petshop.model.Especie;
import crm.petshop.repository.EspecieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EspecieController {

    private final EspecieRepository especieRepository;

    @GetMapping("/especies")
    public List<Especie> listarTodas() {
        return especieRepository.findAllByOrderByNomeEspecieAsc();
    }
}