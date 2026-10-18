package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import vallegrande.edu.pe.backend.model.Provider;
import vallegrande.edu.pe.backend.repository.ProviderRepository;
import vallegrande.edu.pe.backend.util.ReportExportUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderReportService {

    private final ProviderRepository providerRepository;

    private static final String[] HEADERS = {
        "ID", "Tipo Doc.", "Nro. Documento", "Razón Social", "Contacto", "País", "Email", "Teléfono", "Ubigeo", "Certificación"
    };

    private List<String> mapProviderToRow(Provider p) {
        return List.of(
            p.getId() != null ? p.getId() : "",
            p.getDocumentType() != null ? p.getDocumentType() : "",
            p.getDocumentNumber() != null ? p.getDocumentNumber() : "",
            p.getCompanyName() != null ? p.getCompanyName() : "",
            p.getContactName() != null ? p.getContactName() : "",
            p.getCountry() != null ? p.getCountry() : "",
            p.getEmail() != null ? p.getEmail() : "",
            p.getPhone() != null ? p.getPhone() : "",
            p.getUbigeo() != null ? p.getUbigeo() : "",
            p.getCertificationStatus() != null ? p.getCertificationStatus() : ""
        );
    }

    public Mono<byte[]> exportToExcel() {
        return providerRepository.findAll()
            .map(this::mapProviderToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateExcel("Proveedores", HEADERS, rows));
    }

    public Mono<byte[]> exportToPdf() {
        return providerRepository.findAll()
            .map(this::mapProviderToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generatePdf("Padrón de Proveedores de Campo", HEADERS, rows));
    }

    public Mono<byte[]> exportToCsv() {
        return providerRepository.findAll()
            .map(this::mapProviderToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateCsv(HEADERS, rows));
    }
}
