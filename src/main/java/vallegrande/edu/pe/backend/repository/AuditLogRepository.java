package vallegrande.edu.pe.backend.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.AuditLog;

public interface AuditLogRepository extends ReactiveCrudRepository<AuditLog, Long> {
}
