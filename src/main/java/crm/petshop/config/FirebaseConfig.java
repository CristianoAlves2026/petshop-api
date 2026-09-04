package crm.petshop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp inicializarFirebase() throws Exception {
        // ✅ LÊ DIRETO DA VARIÁVEL DE AMBIENTE (NÃO USA ARQUIVO!)
        String conteudoJson = System.getenv("FIREBASE_SERVICE_ACCOUNT");

        if (conteudoJson == null || conteudoJson.isBlank()) {
            throw new RuntimeException("❌ Variável FIREBASE_SERVICE_ACCOUNT NÃO encontrada!");
        }

        ByteArrayInputStream fluxo = new ByteArrayInputStream(
            conteudoJson.getBytes(StandardCharsets.UTF_8)
        );

        FirebaseOptions opcoes = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(fluxo))
            .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(opcoes);
        }
        return FirebaseApp.getInstance();
    }
}