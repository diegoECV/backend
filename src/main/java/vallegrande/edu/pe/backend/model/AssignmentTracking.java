package vallegrande.edu.pe.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("assignment_tracking")
public class AssignmentTracking {
    @Id
    private String id;
    private Integer assignmentId;
    private List<CustodyEvent> events;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustodyEvent {
        private String transferredBy;
        private String transferredTo;
        private Instant transferTime;
        private String condition;
        private String location;
    }
}
