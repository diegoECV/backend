package vallegrande.edu.pe.backend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("SHIPMENT_ITEMS")
public class ShipmentItem {
    @Id
    private Integer itemId;
}
