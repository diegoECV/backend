package vallegrande.edu.pe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vallegrande.edu.pe.backend.model.BatchInspection.InspectionEvent;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDto {
    private Integer batchId;
    private String productCode;
    private LocalDate productionDate;
    private LocalDate expirationDate;
    private List<InspectionEvent> inspections;
}
