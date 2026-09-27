package vallegrande.edu.pe.backend.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import vallegrande.edu.pe.backend.model.EmailVerification;

public interface EmailVerificationRepository extends ReactiveMongoRepository<EmailVerification, String> {
}
