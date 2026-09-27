package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.AuditLog;
import vallegrande.edu.pe.backend.service.AuditLogService;

@RestController
@RequestMapping("/api/v1/auditlogs")
@RequiredArgsConstructor
public class AuditLogRest {
    private final AuditLogService service;

    @GetMapping
    public Flux<AuditLog> findAll(@RequestParam(required = false, defaultValue = "-1") int limit) { return limit > 0 ? service.findAll().take(limit) : service.findAll(); }
    @GetMapping("/{id}")
    public Mono<AuditLog> findById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping
    public Mono<AuditLog> create(@RequestBody AuditLog obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<AuditLog> update(@PathVariable Long id, @RequestBody AuditLog obj) { return service.update(id, obj); }
    @GetMapping("/limit/{limit}")
    public Flux<AuditLog> findLimited(@PathVariable int limit) { return service.findAll().take(limit); }
}