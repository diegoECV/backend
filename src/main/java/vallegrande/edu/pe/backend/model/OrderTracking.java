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
@Document("order_tracking")
public class OrderTracking {
    @Id
    private String id;
    private Integer orderId;
    private List<TrackingEvent> events;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrackingEvent {
        private String status;
        private Instant timestamp;
        private String location;
        private String notes;
    }
}
