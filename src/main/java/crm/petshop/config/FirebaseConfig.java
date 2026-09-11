package crm.petshop.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp inicializarFirebase() throws Exception {
        String valor = System.getenv("FIREBASE_SERVICE_ACCOUNT");
        
        if (valor == null || valor.isBlank()) {
            throw new RuntimeException("❌ Variável FIREBASE_SERVICE_ACCOUNT NÃO encontrada!");
        }

        // ✅ DETECTA AUTOMATICAMENTE: É CAMINHO OU É JSON?
        if (valor.trim().startsWith("{")) {
            // ✅ É JSON DIRETO (como no Render)
            ByteArrayInputStream fluxo = new ByteArrayInputStream(
                valor.getBytes(StandardCharsets.UTF_8)
            );
            FirebaseOptions opcoes = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(fluxo))
                .build();
            
            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(opcoes);
            }
            return FirebaseApp.getInstance();
        } else {
            // ✅ É CAMINHO DE ARQUIVO (como na sua máquina)
            File arquivo = new File(valor.trim());
            if (!arquivo.exists()) {
                throw new RuntimeException("❌ Arquivo NÃO encontrado em: " + valor);
            }
            
            try (FileInputStream fluxo = new FileInputStream(arquivo)) {
                FirebaseOptions opcoes = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(fluxo))
                    .build();
                
                if (FirebaseApp.getApps().isEmpty()) {
                    return FirebaseApp.initializeApp(opcoes);
                }
                return FirebaseApp.getInstance();
            }
        }
    }
}