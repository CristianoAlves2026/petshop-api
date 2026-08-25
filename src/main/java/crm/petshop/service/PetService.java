package crm.petshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import crm.petshop.dto.PetDTO;
import crm.petshop.model.Pet;
import crm.petshop.repository.PetRepository;
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

    // ✅ CADASTRAR PET
    public Pet cadastrar(PetDTO dto) {
        Pet pet = new Pet();
        pet.setNome(dto.getNome());
        pet.setNascimento(dto.getNascimento());
        pet.setIdRaca(dto.getIdRaca());
        pet.setSexo(dto.getSexo());
        pet.setCastrado(dto.getCastrado());
        pet.setFalecido(dto.getFalecido());
        pet.setFoto(dto.getFoto());
        pet.setObservacoes(dto.getObservacoes());
        pet.setIdTutor(dto.getIdTutor());
        return petRepository.save(pet);
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