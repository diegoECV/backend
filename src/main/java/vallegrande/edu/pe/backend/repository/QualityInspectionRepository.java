package vallegrande.edu.pe.backend.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import vallegrande.edu.pe.backend.model.QualityInspection;

public interface QualityInspectionRepository extends ReactiveMongoRepository<QualityInspection, String> {
}
