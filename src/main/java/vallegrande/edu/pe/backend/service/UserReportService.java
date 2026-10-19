package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import vallegrande.edu.pe.backend.model.User;
import vallegrande.edu.pe.backend.repository.UserRepository;
import vallegrande.edu.pe.backend.util.ReportExportUtil;

import java.util.List;

@Service
public class UserReportService {

    private final UserRepository userRepository;

    public UserReportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final String[] HEADERS = {
        "ID", "Usuario", "Nombre Completo", "Email", "Teléfono", "Rol", "Departamento", "Estado", "Último Acceso"
    };

    private List<String> mapUserToRow(User u) {
        return List.of(
            u.getId() != null ? u.getId() : "",
            u.getUsername() != null ? u.getUsername() : "",
            u.getName() != null ? u.getName() : "",
            u.getEmail() != null ? u.getEmail() : "",
            u.getPhone() != null ? u.getPhone() : "",
            u.getRole() != null ? u.getRole() : "",
            u.getDepartment() != null ? u.getDepartment() : "",
            u.getIsActive() != null ? (u.getIsActive() ? "ACTIVO" : "INACTIVO") : "",
            u.getLastLogin() != null ? u.getLastLogin() : ""
        );
    }

    public Mono<byte[]> exportToExcel() {
        return userRepository.findAll()
            .map(this::mapUserToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateExcel("Usuarios", HEADERS, rows));
    }

    public Mono<byte[]> exportToPdf() {
        return userRepository.findAll()
            .map(this::mapUserToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generatePdf("Padrón de Usuarios del Sistema", HEADERS, rows));
    }

    public Mono<byte[]> exportToCsv() {
        return userRepository.findAll()
            .map(this::mapUserToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateCsv(HEADERS, rows));
    }
}
