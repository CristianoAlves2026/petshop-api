package crm.petshop.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import crm.petshop.model.Lancamento;
import crm.petshop.model.Pet;
import crm.petshop.model.Tutor;
import crm.petshop.repository.LancamentoRepository;
import crm.petshop.repository.PetRepository;
import crm.petshop.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final LancamentoRepository lancamentoRepository;
    private final PetRepository petRepository;
    private final TutorRepository tutorRepository;

    public String enviar(String tokenCelular, String titulo, String mensagem) {
        try {
            Message mensagemFirebase = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(titulo)
                    .setBody(mensagem)
                    .build())
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .setToken(tokenCelular)
                .build();

            String resposta = FirebaseMessaging.getInstance().send(mensagemFirebase);
            return "✅ Notificação enviada! ID: " + resposta;
        } catch (FirebaseMessagingException e) {
            return "❌ Erro ao enviar: " + e.getMessage();
        }
    }

       public void verificarEEnviarLembretes() {
    LocalDate hoje = LocalDate.now();
    var todos = lancamentoRepository.buscarAtivosParaNotificar(hoje.plusDays(365));
    
    for (Lancamento lanc : todos) {
        long diasParaVencer = java.time.temporal.ChronoUnit.DAYS.between(hoje, lanc.getRepetir());
        
        String status = lanc.getStatus() != null 
            ? lanc.getStatus().toUpperCase() 
            : "ATIVO";
        if ("IGNORADO".equals(status) || "REPETIDO".equals(status)) {
            continue;
        }
        
        String fraseBase = "";
        if (lanc.getProduto() != null && lanc.getProduto().getFrase() != null 
            && !lanc.getProduto().getFrase().isBlank()) {
            fraseBase = lanc.getProduto().getFrase();
        }
        
        Pet pet = petRepository.findById(lanc.getIdPet()).orElse(null);
        if (pet == null) continue;
        Tutor tutor = tutorRepository.findById(pet.getIdTutor()).orElse(null);
        if (tutor == null || tutor.getTokenFcm() == null || tutor.getTokenFcm().isBlank()) continue;
        
        String nomePet = pet.getNome();
        
        // ✅ Artigo para "de" → do / da
        String artigoDe = "do";
        if ("Fêmea".equalsIgnoreCase(pet.getSexo())) {
            artigoDe = "da";
        }
        String petComDe = artigoDe + " " + nomePet;
        
        // ✅ Artigo para "para" → o / a
        String artigoPara = "o";
        if ("Fêmea".equalsIgnoreCase(pet.getSexo())) {
            artigoPara = "a";
        }
        String petComPara = artigoPara + " " + nomePet;
        
        boolean deveNotificar = false;
        String titulo = "";
        String corpo = "";
        
        if (!fraseBase.isBlank()) {
    String mensagem = fraseBase
        .replace(":petComDe", petComDe)
        .replace(":petComPara", petComPara);
    
    if (diasParaVencer == 15) {
        deveNotificar = true;
        titulo = "⏰ Lembrete";
        corpo = mensagem.replace("X dias", "15 dias"); // ✅ Sem "em"
    } else if (diasParaVencer == 10) {
        deveNotificar = true;
        titulo = "⏰ Lembrete";
        corpo = mensagem.replace("X dias", "10 dias"); // ✅ Sem "em"
    } else if (diasParaVencer == 5) {
        deveNotificar = true;
        titulo = "⏰ Lembrete";
        corpo = mensagem.replace("X dias", "5 dias"); // ✅ Sem "em"
    } else if (diasParaVencer == 1) {
        deveNotificar = true;
        titulo = "⚠️ Atenção!";
        corpo = mensagem.replace("em X dias", "amanhã"); // ✅ Tira o "em"
    } else if (diasParaVencer == 0) {
        deveNotificar = true;
        titulo = "🔴 Hoje!";
        corpo = mensagem.replace("em X dias", "hoje"); // ✅ Tira o "em"
    } else if (diasParaVencer < 0) {
        deveNotificar = true;
        titulo = "⚠️ Vencido!";
        corpo = mensagem.replace("deve acabar em X dias", "acabou")
                       .replace("vence em X dias", "está vencida")
                       .replace("Está chegando a hora", "Chegou a hora");
    }
}
        
        if (deveNotificar) {
            enviarComDados(lanc, titulo, corpo);
        }
    }
    System.out.println("✅ Verificação concluída!");
} 
       

    private void enviarComDados(Lancamento lanc, String titulo, String corpo) {
    Pet pet = petRepository.findById(lanc.getIdPet()).orElse(null);
    if (pet == null) return;
    Tutor tutor = tutorRepository.findById(pet.getIdTutor()).orElse(null);
    if (tutor == null || tutor.getTokenFcm() == null || tutor.getTokenFcm().isBlank()) return;
    
    
    // ✅ PEGA SÓ O PRIMEIRO NOME
    String primeiroNome = tutor.getNome().split(" ")[0];
    String tituloNovo = "🐾 Olá " + primeiroNome + "!!";
    
    try {
        Message mensagem = Message.builder()
            .setNotification(Notification.builder()
                .setTitle(tituloNovo)        // ✅ Novo título personalizado
                .setBody(corpo)              // ✅ Mensagem continua igual
                .build())
            .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
            .putData("idLancamento", lanc.getId().toString())
            .putData("idPet", lanc.getIdPet().toString())
            .setToken(tutor.getTokenFcm())
            .build();
        FirebaseMessaging.getInstance().sendAsync(mensagem).get();
        System.out.println("✅ Notificação enviada — Lançamento: " + lanc.getId() + " → Para: " + tutor.getNome());
    } catch (InterruptedException | ExecutionException e) {
        System.err.println("❌ Erro ao enviar lançamento " + lanc.getId() + ": " + e.getMessage());
        Thread.currentThread().interrupt();
    }
}
}