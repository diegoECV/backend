package vallegrande.edu.pe.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("BATCHES")
public class Batch {
    @Id
    private Integer batchId;
    private String productCode;
    private LocalDate productionDate;
    private LocalDate expirationDate;
}
