package crm.petshop.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    // ✅ ENVIA NOTIFICAÇÃO PARA UM CELULAR ESPECÍFICO
    public String enviar(String tokenCelular, String titulo, String mensagem) {
        try {
            Message mensagemFirebase = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(titulo)
                    .setBody(mensagem)
                    .build())
                .setToken(tokenCelular)
                .build();

            String resposta = FirebaseMessaging.getInstance().send(mensagemFirebase);
            return "✅ Notificação enviada! ID: " + resposta;

        } catch (FirebaseMessagingException e) {
            return "❌ Erro ao enviar: " + e.getMessage();
        }
    }
}