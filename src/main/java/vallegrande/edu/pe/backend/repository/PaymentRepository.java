package vallegrande.edu.pe.backend.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.Payment;

public interface PaymentRepository extends ReactiveCrudRepository<Payment, Integer> {
}
