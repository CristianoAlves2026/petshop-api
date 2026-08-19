package crm.petshop.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TutorDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    private Long idPetshop;

    @NotNull(message = "Município é obrigatório")
    private Long idMunicipio;

    private String bairro;

    @NotBlank(message = "Estado é obrigatório")
    private String estado;
}