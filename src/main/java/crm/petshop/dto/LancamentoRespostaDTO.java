package crm.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoRespostaDTO {

    private Long id;
    private String descricao;      // ✅ NOME DO PRODUTO PRONTO!
    private LocalDate data;
    private LocalDate repetir;
    private String observacao;
    private String foto;
    private String status;
}