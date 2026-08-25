package crm.petshop.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.time.LocalDate;
import crm.petshop.model.Especie;

@Data
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "nascimento")
    private LocalDate nascimento;

    @Column(name = "id_raca")
    private Long idRaca;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_especie")
    private Especie especie;  // ✅ NOVO CAMPO

    @Column(length = 10)
    private String sexo;

    @Column(nullable = false)
    private Boolean castrado = true;

    @Column(nullable = false)
    private Boolean falecido = false;

    @Column(length = 250)
    private String foto;

    @Column(length = 200)
    private String observacoes;

    @Column(name = "id_tutor", nullable = false)
    private Long idTutor;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private Instant criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private Instant atualizadoEm;
}