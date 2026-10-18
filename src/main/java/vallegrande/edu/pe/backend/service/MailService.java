package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

/**
 * Servicio centralizado para el envío de correos HTML con Thymeleaf.
 * Maneja notificaciones de aprobación, invitación y bienvenida.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Envía un correo HTML usando una plantilla Thymeleaf.
     * Se ejecuta en un hilo separado (boundedElastic) para no bloquear el flujo reactivo.
     *
     * @param to           Correo destino
     * @param subject      Asunto del correo
     * @param templateName Nombre de la plantilla (sin extensión .html)
     * @param variables    Mapa de variables para la plantilla
     * @return Mono<Void> que completa cuando el correo fue enviado
     */
    public Mono<Void> sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        return Mono.fromRunnable(() -> {
            try {
                Context context = new Context();
                context.setVariables(variables);
                String htmlContent = templateEngine.process(templateName, context);

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(fromEmail, "Visons S.A.C.");
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                log.info("[MailService] Correo '{}' enviado exitosamente a: {}", subject, to);

            } catch (MessagingException | java.io.UnsupportedEncodingException e) {
                log.error("[MailService] Error al enviar correo a {}: {}", to, e.getMessage());
                throw new RuntimeException("Error al enviar correo: " + e.getMessage(), e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    /**
     * Correo de aprobación de cuenta (Cliente aprobado por el Admin).
     */
    public Mono<Void> sendAprobacionCuenta(String to, String nombre, String rol, String urlLogin) {
        return sendHtmlEmail(
            to,
            "✅ Tu cuenta en Visons fue aprobada",
            "email-aprobacion",
            Map.of("nombre", nombre, "rol", rol, "email", to, "urlLogin", urlLogin)
        );
    }

    /**
     * Correo de invitación a nuevo trabajador (creado por el Admin).
     */
    public Mono<Void> sendInvitacionTrabajador(String to, String nombre, String departamento, String urlActivacion) {
        return sendHtmlEmail(
            to,
            "👋 ¡Bienvenido al equipo Visons! Activa tu cuenta",
            "email-invitacion-trabajador",
            Map.of("nombre", nombre, "departamento", departamento, "email", to, "urlActivacion", urlActivacion)
        );
    }
}
