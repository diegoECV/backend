package vallegrande.edu.pe.backend.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.ShipmentItem;

public interface ShipmentItemRepository extends ReactiveCrudRepository<ShipmentItem, Integer> {
}
