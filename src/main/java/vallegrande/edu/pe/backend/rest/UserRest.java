package vallegrande.edu.pe.backend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.dto.ImportResponse;
import vallegrande.edu.pe.backend.model.User;
import vallegrande.edu.pe.backend.service.UserReportService;
import vallegrande.edu.pe.backend.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserRest {

    private final UserService service;
    private final UserReportService reportService;

    public UserRest(UserService service, UserReportService reportService) {
        this.service = service;
        this.reportService = reportService;
    }

    @GetMapping
    public Flux<User> findAll(@RequestParam(required = false, defaultValue = "-1") int limit) {
        return limit > 0 ? service.findAll().take(limit) : service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<User> findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody User obj) {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Mono<User> update(@PathVariable String id, @RequestBody User obj) {
        return service.update(id, obj);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }

    @PatchMapping("/{id}/toggle-status")
    public Mono<User> toggleStatus(@PathVariable String id) {
        return service.findById(id)
                .flatMap(u -> {
                    boolean current = u.getIsActive() != null && u.getIsActive();
                    u.setIsActive(!current);
                    return service.save(u);
                });
    }

    @PostMapping("/invite-worker")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> inviteWorker(@RequestBody User obj) {
        return service.invitarTrabajador(obj);
    }

    @PutMapping("/{id}/approve")
    public Mono<User> approveUser(@PathVariable String id) {
        return service.aprobarCliente(id);
    }

    // --- REPORTES Y EXPORTACIÓN ---

    @GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> exportExcel() {
        return reportService.exportToExcel()
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios.xlsx")
                        .body(bytes));
    }

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public Mono<ResponseEntity<byte[]>> exportPdf() {
        return reportService.exportToPdf()
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios.pdf")
                        .body(bytes));
    }

    @GetMapping(value = "/export/csv", produces = "text/csv")
    public Mono<ResponseEntity<byte[]>> exportCsv() {
        return reportService.exportToCsv()
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usuarios.csv")
                        .body(bytes));
    }

    // --- CARGA MASIVA E IMPORTACIÓN ---

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ImportResponse>> importUsers(@RequestPart("file") FilePart filePart) {
        return reportService.importFile(filePart)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/import/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ImportResponse>> importUsersExcel(@RequestPart("file") FilePart filePart) {
        return reportService.importFromExcel(filePart)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/import/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ImportResponse>> importUsersCsv(@RequestPart("file") FilePart filePart) {
        return reportService.importFromCsv(filePart)
                .map(ResponseEntity::ok);
    }
}