package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.Shipment;
import vallegrande.edu.pe.backend.service.ShipmentService;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentRest {
    private final ShipmentService service;

    @GetMapping
    public Flux<Shipment> findAll() { return service.findAll(); }
    @GetMapping("/{id}")
    public Mono<Shipment> findById(@PathVariable Integer id) { return service.findById(id); }
    @PostMapping
    public Mono<Shipment> create(@RequestBody Shipment obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<Shipment> update(@PathVariable Integer id, @RequestBody Shipment obj) { return service.update(id, obj); }
}
