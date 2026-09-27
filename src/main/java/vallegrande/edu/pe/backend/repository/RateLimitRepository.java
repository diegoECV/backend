package vallegrande.edu.pe.backend.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import vallegrande.edu.pe.backend.model.RateLimit;

public interface RateLimitRepository extends ReactiveMongoRepository<RateLimit, String> {
}
