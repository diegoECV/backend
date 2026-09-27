package vallegrande.edu.pe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vallegrande.edu.pe.backend.model.AssignmentTracking.CustodyEvent;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {
    private Integer assignmentId;
    private Integer orderId;
    private String assignedTo;
    private LocalDate assignmentDate;
    private String status;
    private List<CustodyEvent> custodyEvents;
}
