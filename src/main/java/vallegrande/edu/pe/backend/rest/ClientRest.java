package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.Client;
import vallegrande.edu.pe.backend.service.ClientService;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientRest {
    private final ClientService service;

    @GetMapping
    public Flux<Client> findAll() { return service.findAll(); }
    @GetMapping("/{id}")
    public Mono<Client> findById(@PathVariable String id) { return service.findById(id); }
    @PostMapping
    public Mono<Client> create(@RequestBody Client obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<Client> update(@PathVariable String id, @RequestBody Client obj) { return service.update(id, obj); }
}
