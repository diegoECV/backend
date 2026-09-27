package vallegrande.edu.pe.backend.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import vallegrande.edu.pe.backend.model.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
