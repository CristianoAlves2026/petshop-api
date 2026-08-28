package crm.petshop.controller;

import crm.petshop.model.Produto;
import crm.petshop.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    // ✅ LISTAR TODOS OS PRODUTOS
    @GetMapping
    public List<Produto> listarTodos() {
        return produtoRepository.findAllByOrderByDescricaoAsc();
    }

    // ✅ BUSCAR POR NOME (EX: ?nome=vacina)
    @GetMapping("/buscar")
    public List<Produto> buscar(@RequestParam String nome) {
        return produtoRepository.findByDescricaoContainingIgnoreCaseOrderByDescricaoAsc(nome);
    }

      // ✅ 1=Vacinas / 2=Alimentação / 3=Saúde / 4=Higiene / 5=Outros
    @GetMapping("/categoria/{idProduto}")
    public List<Produto> buscarPorCategoria(@PathVariable Integer idProduto) {
        return produtoRepository.findByIdProdutoOrderByDescricaoAsc(idProduto);
    }
}