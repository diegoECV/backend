package vallegrande.edu.pe.backend.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vallegrande.edu.pe.backend.model.Assignment;

public interface AssignmentRepository extends ReactiveCrudRepository<Assignment, Integer> {
}
