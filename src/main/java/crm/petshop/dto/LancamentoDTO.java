package crm.petshop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LancamentoDTO {

    private Long id;
    private Long idLancamentoRepetido;

    @NotNull(message = "Pet é obrigatório")
    private Long idPet;

    @NotNull(message = "Produto é obrigatório")
    private Long idProduto;

    private Integer idPetshops; // ✅ PODE SER NULO

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    private String observacao;

    private LocalDate repetir;

    private String foto;

    private String status = "ativo";
}