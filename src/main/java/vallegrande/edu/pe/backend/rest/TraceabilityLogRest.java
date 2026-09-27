package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.TraceabilityLog;
import vallegrande.edu.pe.backend.service.TraceabilityLogService;

@RestController
@RequestMapping("/api/v1/traceability_logs")
@RequiredArgsConstructor
public class TraceabilityLogRest {
    private final TraceabilityLogService service;

    @GetMapping
    public Flux<TraceabilityLog> findAll() { return service.findAll(); }
    @GetMapping("/{id}")
    public Mono<TraceabilityLog> findById(@PathVariable String id) { return service.findById(id); }
    @PostMapping
    public Mono<TraceabilityLog> create(@RequestBody TraceabilityLog obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<TraceabilityLog> update(@PathVariable String id, @RequestBody TraceabilityLog obj) { return service.update(id, obj); }
}
