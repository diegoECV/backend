package vallegrande.edu.pe.backend.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.Shipment;

public interface ShipmentRepository extends ReactiveCrudRepository<Shipment, Integer> {
}
