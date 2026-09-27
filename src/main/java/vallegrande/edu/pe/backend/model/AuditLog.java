package vallegrande.edu.pe.backend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("AUDIT_LOGS")
public class AuditLog {
    @Id
    private Long id;
}
