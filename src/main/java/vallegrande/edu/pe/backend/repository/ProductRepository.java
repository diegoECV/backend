package vallegrande.edu.pe.backend.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import vallegrande.edu.pe.backend.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
