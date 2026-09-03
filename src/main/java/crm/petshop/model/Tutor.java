package crm.petshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tutores")
@Data
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, length = 11)
    private String telefone;

    @Column(nullable = false, length = 200)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    private Long idPetshop;

    @Column(name = "id_municipio")
    private Long idMunicipio;

    @Column(length = 150)
    private String bairro;

    @Column(length = 2)
    private String estado;

    // ✅ Token do Firebase Cloud Messaging (para notificações)
    @Column(name = "token_fcm")
    private String tokenFcm;

}