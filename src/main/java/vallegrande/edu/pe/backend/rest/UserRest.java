package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.User;
import vallegrande.edu.pe.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRest {
    private final UserService service;

    @GetMapping
    public Flux<User> findAll() { return service.findAll(); }
    @GetMapping("/{id}")
    public Mono<User> findById(@PathVariable String id) { return service.findById(id); }
    @PostMapping
    public Mono<User> create(@RequestBody User obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<User> update(@PathVariable String id, @RequestBody User obj) { return service.update(id, obj); }
}
