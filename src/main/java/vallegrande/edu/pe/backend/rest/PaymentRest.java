package vallegrande.edu.pe.backend.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.model.Payment;
import vallegrande.edu.pe.backend.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentRest {
    private final PaymentService service;

    @GetMapping
    public Flux<Payment> findAll() { return service.findAll(); }
    @GetMapping("/{id}")
    public Mono<Payment> findById(@PathVariable Integer id) { return service.findById(id); }
    @PostMapping
    public Mono<Payment> create(@RequestBody Payment obj) { return service.save(obj); }
    @PutMapping("/{id}")
    public Mono<Payment> update(@PathVariable Integer id, @RequestBody Payment obj) { return service.update(id, obj); }
}
