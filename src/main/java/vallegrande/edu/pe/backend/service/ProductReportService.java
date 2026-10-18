package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import vallegrande.edu.pe.backend.model.Product;
import vallegrande.edu.pe.backend.repository.ProductRepository;
import vallegrande.edu.pe.backend.util.ReportExportUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReportService {

    private final ProductRepository productRepository;

    private static final String[] HEADERS = {
        "ID", "Código", "Nombre", "Categoría", "Variedad", "Calibre", "Unidad", "Partida Arancelaria (HS)", "Activo"
    };

    private List<String> mapProductToRow(Product p) {
        return List.of(
            p.getId() != null ? p.getId() : "",
            p.getCode() != null ? p.getCode() : "",
            p.getName() != null ? p.getName() : "",
            p.getCategoryId() != null ? p.getCategoryId() : "",
            p.getVariety() != null ? p.getVariety() : "",
            p.getCaliber() != null ? p.getCaliber() : "",
            p.getUnitMeasure() != null ? p.getUnitMeasure() : "",
            p.getHsCode() != null ? p.getHsCode() : "",
            p.getIsActive() != null ? (p.getIsActive() ? "SÍ" : "NO") : ""
        );
    }

    public Mono<byte[]> exportToExcel() {
        return productRepository.findAll()
            .map(this::mapProductToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateExcel("Productos", HEADERS, rows));
    }

    public Mono<byte[]> exportToPdf() {
        return productRepository.findAll()
            .map(this::mapProductToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generatePdf("Catálogo de Productos de Exportación", HEADERS, rows));
    }

    public Mono<byte[]> exportToCsv() {
        return productRepository.findAll()
            .map(this::mapProductToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateCsv(HEADERS, rows));
    }
}
