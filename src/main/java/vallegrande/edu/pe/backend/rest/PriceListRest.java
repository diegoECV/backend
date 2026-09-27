package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.PriceList;
import vallegrande.edu.pe.backend.service.PriceListService;

@RestController
@RequestMapping("/api/v1/price_lists")
@RequiredArgsConstructor
public class PriceListRest {
    private final PriceListService service;

    @GetMapping
    public Flux<PriceList> findAll() { return service.findAll(); }
    @GetMapping("/{id}")
    public Mono<PriceList> findById(@PathVariable String id) { return service.findById(id); }
    @PostMapping
    public Mono<PriceList> create(@RequestBody PriceList obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<PriceList> update(@PathVariable String id, @RequestBody PriceList obj) { return service.update(id, obj); }
}
