package vallegrande.edu.pe.backend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("PAYMENTS")
public class Payment {
    @Id
    private Integer paymentId;
}
