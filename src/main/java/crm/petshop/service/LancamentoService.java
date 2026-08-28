package crm.petshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import crm.petshop.dto.LancamentoDTO;
import crm.petshop.model.Lancamento;
import crm.petshop.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import crm.petshop.repository.ProdutoRepository; // ✅ NOVA LINHA

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final Cloudinary cloudinary;
    private final ProdutoRepository produtoRepository; // ✅ NOVA LINHA

       // ✅ CADASTRAR LANÇAMENTO
        // ✅ CADASTRAR LANÇAMENTO
    public Lancamento cadastrar(LancamentoDTO dto) {
        Lancamento lancamento = new Lancamento();
        lancamento.setIdPet(dto.getIdPet());
        lancamento.setIdProduto(dto.getIdProduto());
        lancamento.setIdPetshops(dto.getIdPetshops());
        lancamento.setData(dto.getData());
        lancamento.setObservacao(dto.getObservacao());
        lancamento.setRepetir(dto.getRepetir());
        lancamento.setFoto(dto.getFoto());
        // ❌ SEM setIdLancamentoRepetido — NÃO SALVA NA TABELA!
        lancamento.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : "ATIVO");
        
        Lancamento salvo = lancamentoRepository.save(lancamento); // ✅ SALVA SEM O CAMPO EXTRA

        // ✅ SE TIVER O ID → MUDA O ANTIGO PARA REPETIDO (SÓ EM MEMÓRIA!)
        if (dto.getIdLancamentoRepetido() != null) {
            try {
                Lancamento antigo = lancamentoRepository.findById(dto.getIdLancamentoRepetido()).orElse(null);
                if (antigo != null) {
                    antigo.setStatus("REPETIDO"); // ✅ SÓ MUDA O STATUS
                    lancamentoRepository.save(antigo); // ✅ SÓ SALVA A ALTERAÇÃO DO ANTIGO
                }
            } catch (Exception e) {
                // Segue normalmente se der erro
            }
        }
        return salvo;
    }

    // ✅ ATUALIZAR LANÇAMENTO
    public Lancamento atualizar(Long id, LancamentoDTO dto) {
        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Lançamento não encontrado"));
        lancamento.setIdPet(dto.getIdPet());
        lancamento.setIdProduto(dto.getIdProduto());
        lancamento.setIdPetshops(dto.getIdPetshops());
        lancamento.setData(dto.getData());
        lancamento.setObservacao(dto.getObservacao());
        lancamento.setRepetir(dto.getRepetir());
        lancamento.setFoto(dto.getFoto());
        //lancamento.setStatus(dto.getStatus() != null ? dto.getStatus().toUpperCase() : "ATIVO");
        return lancamentoRepository.save(lancamento);
    }

    // ✅ EXCLUIR LANÇAMENTO
    public void excluir(Long id) {
        if (!lancamentoRepository.existsById(id)) {
            throw new RuntimeException("❌ Lançamento não encontrado");
        }
        lancamentoRepository.deleteById(id);
    }

        // ✅ LISTAR LANÇAMENTOS POR PET — TUDO IGUAL + NOME DO PRODUTO!
    public List<Lancamento> listarPorPet(Long idPet) {
        List<Lancamento> lista = lancamentoRepository.findByIdPetOrderByDataDesc(idPet);
        
        // ✅ ==== SÓ ACRESCENTAMOS ESTA PARTE ABAIXO ====
        // ✅ Buscamos o nome do produto e guardamos junto
        for (Lancamento lancamento : lista) {
            if (lancamento.getIdProduto() != null) {
                try {
                    crm.petshop.model.Produto produto = produtoRepository.findById(lancamento.getIdProduto()).orElse(null);
                    if (produto != null) {
                        // ✅ Colocamos o nome em um campo extra — SEM APAGAR NADA!
                        lancamento.setDescricao(produto.getDescricao());
                    }
                } catch (Exception e) {
                    // Se der erro, não faz nada — o resto continua funcionando!
                }
            }
        }
        // ✅ ==== FIM DA PARTE ACRESCENTADA ====
        
        return lista; // ✅ CONTINUA RETORNANDO A LISTA NORMAL!
    }

    // ✅ BUSCAR LANÇAMENTO POR ID
    public Lancamento buscarPorId(Long id) {
        return lancamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Lançamento não encontrado"));
    }

    // ✅ SALVAR ENTIDADE DIRETO (para o método "Ignorar")
    public Lancamento salvarEntidade(Lancamento lancamento) {
        return lancamentoRepository.save(lancamento);
    }

    // ✅ ENVIAR FOTO PARA O CLOUDINARY
    public String enviarFoto(MultipartFile arquivo) throws IOException {
        Map upload = cloudinary.uploader().upload(
                arquivo.getBytes(),
                ObjectUtils.asMap(
                        "folder", "lancamentos/",
                        "resource_type", "image"
                )
        );
        return upload.get("secure_url").toString();
    }
}