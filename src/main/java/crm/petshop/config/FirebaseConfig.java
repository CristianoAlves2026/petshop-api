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
        // ✅ LÊ A CHAVE DA VARIÁVEL DE AMBIENTE (NÃO USA ARQUIVO!)
        String chaveJson = System.getenv("FIREBASE_SERVICE_ACCOUNT");
        
        if (chaveJson == null || chaveJson.isBlank()) {
            throw new RuntimeException("Variável FIREBASE_SERVICE_ACCOUNT NÃO configurada no Render!");
        }

        ByteArrayInputStream fluxo = new ByteArrayInputStream(
            chaveJson.getBytes(StandardCharsets.UTF_8)
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