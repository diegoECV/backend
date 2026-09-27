package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.TemperatureLog;
import vallegrande.edu.pe.backend.service.TemperatureLogService;

@RestController
@RequestMapping("/api/v1/temperature_logs")
@RequiredArgsConstructor
public class TemperatureLogRest {
    private final TemperatureLogService service;

    @GetMapping
    public Flux<TemperatureLog> findAll(@RequestParam(required = false, defaultValue = "-1") int limit) { return limit > 0 ? service.findAll().take(limit) : service.findAll(); }
    @GetMapping("/{id}")
    public Mono<TemperatureLog> findById(@PathVariable String id) { return service.findById(id); }
    @PostMapping
    public Mono<TemperatureLog> create(@RequestBody TemperatureLog obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<TemperatureLog> update(@PathVariable String id, @RequestBody TemperatureLog obj) { return service.update(id, obj); }
    @GetMapping("/limit/{limit}")
    public Flux<TemperatureLog> findLimited(@PathVariable int limit) { return service.findAll().take(limit); }
}