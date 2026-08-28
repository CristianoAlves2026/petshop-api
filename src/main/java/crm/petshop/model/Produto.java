package crm.petshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_produto", nullable = false) // ✅ NOVO CAMPO
    private Integer idProduto;

    @Column(nullable = false, length = 50)
    private String descricao;
}