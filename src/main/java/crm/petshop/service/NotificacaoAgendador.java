package crm.petshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificacaoAgendador {

    private final NotificacaoService notificacaoService;

    // ✅ TODOS OS DIAS ÀS 12:00h (horário do servidor)
    @Scheduled(cron = "0 0 12 * * ?")
    public void executarVerificacaoDiaria() {
        System.out.println("🔔 Iniciando verificação de lembretes às 12:00h...");
        notificacaoService.verificarEEnviarLembretes();
        System.out.println("✅ Verificação concluída!");
    }
}