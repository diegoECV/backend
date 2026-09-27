package vallegrande.edu.pe.backend.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.Invoice;

public interface InvoiceRepository extends ReactiveCrudRepository<Invoice, Integer> {
}
