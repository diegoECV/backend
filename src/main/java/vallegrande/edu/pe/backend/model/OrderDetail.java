package vallegrande.edu.pe.backend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("ORDER_DETAILS")
public class OrderDetail {
    @Id
    private Integer orderDetailId;
}
