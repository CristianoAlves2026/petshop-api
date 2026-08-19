package crm.petshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cidades")
@Data
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Coluna no banco se chama "cidade", NÃO "nome"!
    @Column(name = "cidade")
    private String nome;

    // ✅ Coluna "estado" está correta!
    @Column(name = "estado")
    private String estado;
}