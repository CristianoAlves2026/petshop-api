package crm.petshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificacaoAgendador {
    private final NotificacaoService notificacaoService;

    // ✅ TESTE — Todos os dias às 15:30h
    @Scheduled(cron = "0 30 16 * * ?", zone = "America/Sao_Paulo")
    public void executarVerificacaoDiaria() {
        System.out.println("🔔 Iniciando verificação de lembretes às 15:30h...");
        notificacaoService.verificarEEnviarLembretes();
        System.out.println("✅ Verificação concluída!");
    }
}