package vallegrande.edu.pe.backend.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.Certificate;

public interface CertificateRepository extends ReactiveCrudRepository<Certificate, Integer> {
}
