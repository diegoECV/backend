package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.QualityInspection;
import vallegrande.edu.pe.backend.service.QualityInspectionService;

@RestController
@RequestMapping("/api/v1/quality_inspections")
@RequiredArgsConstructor
public class QualityInspectionRest {
    private final QualityInspectionService service;

    @GetMapping
    public Flux<QualityInspection> findAll() { return service.findAll(); }
    @GetMapping("/{id}")
    public Mono<QualityInspection> findById(@PathVariable String id) { return service.findById(id); }
    @PostMapping
    public Mono<QualityInspection> create(@RequestBody QualityInspection obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<QualityInspection> update(@PathVariable String id, @RequestBody QualityInspection obj) { return service.update(id, obj); }
}
