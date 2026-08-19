package crm.petshop.controller;

import crm.petshop.model.Cidade;
import crm.petshop.repository.CidadeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CidadeController {

    private final CidadeRepository repository;

    // ✅ Injeção via CONSTRUTOR (sem @Autowired no campo)
    public CidadeController(CidadeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/estados")
    public List<String> listarEstados() {
        return repository.buscarTodosEstados();
    }

    @GetMapping("/municipios")
    public List<Cidade> listarMunicipios(@RequestParam String estado) {
        return repository.buscarPorEstado(estado);
    }
}