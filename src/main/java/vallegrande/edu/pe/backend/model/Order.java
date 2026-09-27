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
@Table("ORDERS")
public class Order {
    @Id
    private Integer orderId;
    private String clientId;
    private String orderCode;
    private LocalDate orderDate;
    private String incoterm;
    private String status;
    private String statusDescription;
}
