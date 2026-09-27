package vallegrande.edu.pe.backend.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import vallegrande.edu.pe.backend.model.TraceabilityLog;

public interface TraceabilityLogRepository extends ReactiveMongoRepository<TraceabilityLog, String> {
}
