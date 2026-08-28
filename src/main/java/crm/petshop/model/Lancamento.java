package crm.petshop.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "lancamentos")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pet")
    private Long idPet;

    @Column(name = "id_produto")
    private Long idProduto;

     @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produto", insertable = false, updatable = false)
    private Produto produto;

    @Column(name = "id_petshops")
    private Integer idPetshops;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "repetir")
    private LocalDate repetir;

    @Column(name = "foto", length = 255)
    private String foto;

    @Column(name = "status", length = 20)
    private String status = "ativo";

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private Instant atualizadoEm;

    // ✅ CAMPO EXTRA PARA O NOME DO PRODUTO
    @Transient // ✅ NÃO SALVA NO BANCO — SÓ VAI PARA A RESPOSTA
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}