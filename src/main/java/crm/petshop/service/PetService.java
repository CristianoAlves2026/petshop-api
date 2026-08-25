package crm.petshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import crm.petshop.dto.PetDTO;
import crm.petshop.model.Pet;
import crm.petshop.model.Especie;
import crm.petshop.repository.PetRepository;
import crm.petshop.repository.EspecieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final Cloudinary cloudinary;
    private final EspecieRepository especieRepository;

    // ✅ CADASTRAR PET
    public Pet cadastrar(PetDTO dto) {
        Pet pet = new Pet();
        pet.setNome(dto.getNome());
        pet.setNascimento(dto.getNascimento());
        pet.setIdRaca(dto.getIdRaca());

        // ✅ ESPÉCIE
        if (dto.getIdEspecie() != null) {
            Especie especie = especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("❌ Espécie não encontrada"));
            pet.setEspecie(especie);
        }

        pet.setSexo(dto.getSexo());
        pet.setCastrado(dto.getCastrado());
        pet.setFalecido(dto.getFalecido());
        pet.setFoto(dto.getFoto());
        pet.setObservacoes(dto.getObservacoes());
        pet.setIdTutor(dto.getIdTutor());
        return petRepository.save(pet);
    }

    // ✅ ATUALIZAR PET
    public Pet atualizar(Long id, PetDTO dto) {
        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("❌ Pet não encontrado"));

        pet.setNome(dto.getNome());
        pet.setNascimento(dto.getNascimento());
        pet.setIdRaca(dto.getIdRaca());

        // ✅ ATUALIZA ESPÉCIE
        if (dto.getIdEspecie() != null) {
            Especie especie = especieRepository.findById(dto.getIdEspecie())
                .orElseThrow(() -> new RuntimeException("❌ Espécie não encontrada"));
            pet.setEspecie(especie);
        } else {
            pet.setEspecie(null);
        }

        pet.setSexo(dto.getSexo());
        pet.setCastrado(dto.getCastrado());
        pet.setFalecido(dto.getFalecido());
        pet.setFoto(dto.getFoto());
        pet.setObservacoes(dto.getObservacoes());
        pet.setIdTutor(dto.getIdTutor());

        return petRepository.save(pet);
    }

    // ✅ EXCLUIR PET
    public void excluir(Long id) {
        if (!petRepository.existsById(id)) {
            throw new RuntimeException("❌ Pet não encontrado");
        }
        petRepository.deleteById(id);
    }

    // ✅ LISTAR PETS DO TUTOR
    public List<Pet> listarPorTutor(Long idTutor) {
        return petRepository.findByIdTutorOrderByNomeAsc(idTutor);
    }

    // ✅ ENVIAR FOTO PARA O CLOUDINARY
    public String enviarFoto(MultipartFile arquivo) throws IOException {
        Map upload = cloudinary.uploader().upload(
            arquivo.getBytes(),
            ObjectUtils.asMap(
                "folder", "pets/",
                "resource_type", "image"
            )
        );
        return upload.get("secure_url").toString();
    }
}