package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.ShipmentItem;
import vallegrande.edu.pe.backend.service.ShipmentItemService;

@RestController
@RequestMapping("/api/v1/shipmentitems")
@RequiredArgsConstructor
public class ShipmentItemRest {
    private final ShipmentItemService service;

    @GetMapping
    public Flux<ShipmentItem> findAll(@RequestParam(required = false, defaultValue = "-1") int limit) { return limit > 0 ? service.findAll().take(limit) : service.findAll(); }
    @GetMapping("/{id}")
    public Mono<ShipmentItem> findById(@PathVariable Integer id) { return service.findById(id); }
    @PostMapping
    public Mono<ShipmentItem> create(@RequestBody ShipmentItem obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<ShipmentItem> update(@PathVariable Integer id, @RequestBody ShipmentItem obj) { return service.update(id, obj); }
    @GetMapping("/limit/{limit}")
    public Flux<ShipmentItem> findLimited(@PathVariable int limit) { return service.findAll().take(limit); }
}