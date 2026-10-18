package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.User;
import vallegrande.edu.pe.backend.repository.UserRepository;

/**
 * Servicio de gestión de usuarios.
 * Incluye CRUD completo + lógica de aprobación/invitación con notificación por correo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final MailService mailService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    // ===================== CRUD BÁSICO =====================

    public Flux<User> findAll() { return repository.findAll(); }

    public Mono<User> findById(String id) { return repository.findById(id); }

    public Mono<User> save(User obj) { return repository.save(obj); }

    public Mono<User> update(String id, User obj) {
        obj.setId(id);
        return repository.save(obj);
    }

    public Mono<Void> delete(String id) { return repository.deleteById(id); }

    // ===================== LÓGICA DE APROBACIÓN =====================

    /**
     * Aprueba la cuenta de un Cliente (cambia status PENDING → ACTIVE)
     * y le envía un correo de confirmación automáticamente.
     */
    public Mono<User> aprobarCliente(String userId) {
        return repository.findById(userId)
            .flatMap(user -> {
                user.setIsActive(true);
                return repository.save(user)
                    .flatMap(userGuardado -> {
                        String urlLogin = frontendUrl + "/login";
                        return mailService.sendAprobacionCuenta(
                            userGuardado.getEmail(),
                            userGuardado.getName(),
                            userGuardado.getRole(),
                            urlLogin
                        ).thenReturn(userGuardado);
                    });
            })
            .doOnSuccess(u -> log.info("[UserService] Cliente aprobado y correo enviado: {}", u.getEmail()))
            .doOnError(e -> log.error("[UserService] Error al aprobar cliente: {}", e.getMessage()));
    }

    /**
     * Crea la cuenta de un Trabajador (invitado por el Admin)
     * y le envía un correo con el enlace de activación.
     */
    public Mono<User> invitarTrabajador(User nuevoTrabajador) {
        nuevoTrabajador.setIsActive(false); // Inactivo hasta que active su cuenta
        nuevoTrabajador.setRole("WORKER");

        return repository.save(nuevoTrabajador)
            .flatMap(trabajador -> {
                // Token simple: id del usuario en base64 (en producción usar UUID + expiración)
                String token = java.util.Base64.getEncoder()
                    .encodeToString(trabajador.getId().getBytes());
                String urlActivacion = frontendUrl + "/activar-cuenta?token=" + token;

                return mailService.sendInvitacionTrabajador(
                    trabajador.getEmail(),
                    trabajador.getName(),
                    trabajador.getDepartment() != null ? trabajador.getDepartment() : "Operaciones",
                    urlActivacion
                ).thenReturn(trabajador);
            })
            .doOnSuccess(t -> log.info("[UserService] Trabajador invitado y correo enviado: {}", t.getEmail()))
            .doOnError(e -> log.error("[UserService] Error al invitar trabajador: {}", e.getMessage()));
    }

    /**
     * Activa la cuenta de un trabajador usando su ID (decodificado del token).
     */
    public Mono<User> activarCuentaTrabajador(String userId, String nuevaPassword) {
        return repository.findById(userId)
            .flatMap(user -> {
                // En producción, la password debe pasar por BCryptPasswordEncoder
                user.setPassword(nuevaPassword);
                user.setIsActive(true);
                return repository.save(user);
            })
            .doOnSuccess(u -> log.info("[UserService] Cuenta activada exitosamente: {}", u.getEmail()));
    }

    /**
     * Retorna todos los usuarios con cuenta pendiente de aprobación.
     */
    public Flux<User> findPendientes() {
        return repository.findAll()
            .filter(user -> Boolean.FALSE.equals(user.getIsActive()));
    }
}
