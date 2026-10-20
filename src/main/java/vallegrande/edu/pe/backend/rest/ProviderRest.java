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
import vallegrande.edu.pe.backend.model.Provider;
import vallegrande.edu.pe.backend.service.ProviderReportService;
import vallegrande.edu.pe.backend.service.ProviderService;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderRest {

    private final ProviderService service;
    private final ProviderReportService reportService;

    @GetMapping
    public Flux<Provider> findAll(@RequestParam(required = false, defaultValue = "-1") int limit) {
        return limit > 0 ? service.findAll().take(limit) : service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Provider> findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Provider> create(@RequestBody Provider obj) {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Mono<Provider> update(@PathVariable String id, @RequestBody Provider obj) {
        return service.update(id, obj);
    }

    // --- REPORTES Y EXPORTACIÓN ---

    @GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<byte[]>> exportExcel() {
        return reportService.exportToExcel()
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=proveedores.xlsx")
                        .body(bytes));
    }

    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public Mono<ResponseEntity<byte[]>> exportPdf() {
        return reportService.exportToPdf()
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=proveedores.pdf")
                        .body(bytes));
    }

    @GetMapping(value = "/export/csv", produces = "text/csv")
    public Mono<ResponseEntity<byte[]>> exportCsv() {
        return reportService.exportToCsv()
                .map(bytes -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=proveedores.csv")
                        .body(bytes));
    }

    // --- CARGA MASIVA E IMPORTACIÓN ---

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ImportResponse>> importProviders(@RequestPart("file") FilePart filePart) {
        return reportService.importFile(filePart)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/import/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ImportResponse>> importProvidersExcel(@RequestPart("file") FilePart filePart) {
        return reportService.importFromExcel(filePart)
                .map(ResponseEntity::ok);
    }

    @PostMapping(value = "/import/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ImportResponse>> importProvidersCsv(@RequestPart("file") FilePart filePart) {
        return reportService.importFromCsv(filePart)
                .map(ResponseEntity::ok);
    }
}