package crm.petshop.repository;

import crm.petshop.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // ✅ BUSCA POR DESCRIÇÃO (PARCIAL — para pesquisa!)
    List<Produto> findByDescricaoContainingIgnoreCaseOrderByDescricaoAsc(String descricao);

    // ✅ LISTA TODAS ORDENADAS
    List<Produto> findAllByOrderByDescricaoAsc();

    // ✅ Buscar produtos por categoria (1=Vacinas, 2=Alimentação, etc)
    List<Produto> findByIdProdutoOrderByDescricaoAsc(Integer idProduto);
}