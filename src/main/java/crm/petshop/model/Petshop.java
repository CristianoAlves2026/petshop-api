package crm.petshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "petshops")
public class Petshop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @Column(name = "fone")
    private String fone;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "cidade", length = 50)
    private String cidade;

    @Column(name = "endereco", length = 150)
    private String endereco;

    // ✅ NOVO CAMPO
    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "status", length = 20)
    private String status = "ativo";
}