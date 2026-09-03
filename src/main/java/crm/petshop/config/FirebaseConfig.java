package crm.petshop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials}")
    private String caminhoChave;

    @Bean
    public FirebaseApp inicializarFirebase() throws IOException {
        // ✅ LÊ O ARQUIVO DA CHAVE QUE VOCÊ COLOCOU
        ClassPathResource recurso = new ClassPathResource(
            "petshop-app-c13da-firebase-adminsdk-fbsvc-aae4309a37.json"
        );

        FirebaseOptions opcoes = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(recurso.getInputStream()))
            .build();

        // ✅ VERIFICA SE JÁ NÃO FOI INICIALIZADO
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(opcoes);
        }
        return FirebaseApp.getInstance();
    }
}