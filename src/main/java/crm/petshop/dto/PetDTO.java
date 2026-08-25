package crm.petshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PetDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private LocalDate nascimento;

    private Long idRaca;

     // ✅ ==== ADICIONE ESTA LINHA ABAIXO ====
    @NotNull(message = "Espécie é obrigatória")
    private Long idEspecie;
    // ✅ ==== FIM DA ALTERAÇÃO ====

    private String sexo;

    private Boolean castrado = true;

    private Boolean falecido = false;

    private String foto;

    private String observacoes;

    @NotNull(message = "Tutor é obrigatório")
    private Long idTutor;
}