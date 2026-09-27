package vallegrande.edu.pe.backend.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.CustomsDeclaration;

public interface CustomsDeclarationRepository extends ReactiveCrudRepository<CustomsDeclaration, Integer> {
}
