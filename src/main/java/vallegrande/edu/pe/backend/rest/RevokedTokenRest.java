package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.RevokedToken;
import vallegrande.edu.pe.backend.service.RevokedTokenService;

@RestController
@RequestMapping("/api/v1/revoked_tokens")
@RequiredArgsConstructor
public class RevokedTokenRest {
    private final RevokedTokenService service;

    @GetMapping
    public Flux<RevokedToken> findAll(@RequestParam(required = false, defaultValue = "-1") int limit) { return limit > 0 ? service.findAll().take(limit) : service.findAll(); }
    @GetMapping("/{id}")
    public Mono<RevokedToken> findById(@PathVariable String id) { return service.findById(id); }
    @PostMapping
    public Mono<RevokedToken> create(@RequestBody RevokedToken obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<RevokedToken> update(@PathVariable String id, @RequestBody RevokedToken obj) { return service.update(id, obj); }
}
