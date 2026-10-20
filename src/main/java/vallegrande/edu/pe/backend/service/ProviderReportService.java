package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import vallegrande.edu.pe.backend.dto.ImportResponse;
import vallegrande.edu.pe.backend.model.Provider;
import vallegrande.edu.pe.backend.repository.ProviderRepository;
import vallegrande.edu.pe.backend.util.ReportExportUtil;
import vallegrande.edu.pe.backend.util.ReportImportUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private Provider mapRowToProvider(List<String> r) {
        String id = r.size() > 0 && !r.get(0).isBlank() ? r.get(0) : "PROV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String docType = r.size() > 1 && !r.get(1).isBlank() ? r.get(1) : "RUC";
        String docNum = r.size() > 2 ? r.get(2) : "";
        String company = r.size() > 3 ? r.get(3) : "";
        String contact = r.size() > 4 ? r.get(4) : "";
        String country = r.size() > 5 ? r.get(5) : "Peru";
        String email = r.size() > 6 ? r.get(6) : "";
        String phone = r.size() > 7 ? r.get(7) : "";
        String ubigeo = r.size() > 8 ? r.get(8) : "110101";
        String cert = r.size() > 9 ? r.get(9) : "GLOBAL_GAP_CERTIFIED";

        return Provider.builder()
                .id(id)
                .documentType(docType)
                .documentNumber(docNum)
                .companyName(company)
                .contactName(contact)
                .country(country)
                .email(email)
                .phone(phone)
                .address("Fundo de Producción Agrícola")
                .ubigeo(ubigeo)
                .bankAccountCci("002-191-000000000000-00")
                .certificationStatus(cert)
                .build();
    }

    // --- EXPORTACIÓN ---

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

    // --- IMPORTACIÓN ---

    private Mono<byte[]> extractBytes(FilePart filePart) {
        return DataBufferUtils.join(filePart.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                });
    }

    public Mono<ImportResponse> importFromExcel(FilePart filePart) {
        return extractBytes(filePart)
                .publishOn(Schedulers.boundedElastic())
                .map(bytes -> {
                    try {
                        return ReportImportUtil.parseExcel(bytes);
                    } catch (Exception e) {
                        throw new RuntimeException("Error al parsear Excel de proveedores: " + e.getMessage(), e);
                    }
                })
                .flatMap(this::saveImportedRows);
    }

    public Mono<ImportResponse> importFromCsv(FilePart filePart) {
        return extractBytes(filePart)
                .publishOn(Schedulers.boundedElastic())
                .map(bytes -> {
                    try {
                        return ReportImportUtil.parseCsv(bytes);
                    } catch (Exception e) {
                        throw new RuntimeException("Error al parsear CSV de proveedores: " + e.getMessage(), e);
                    }
                })
                .flatMap(this::saveImportedRows);
    }

    public Mono<ImportResponse> importFile(FilePart filePart) {
        String filename = filePart.filename().toLowerCase();
        if (filename.endsWith(".csv")) {
            return importFromCsv(filePart);
        } else {
            return importFromExcel(filePart);
        }
    }

    private Mono<ImportResponse> saveImportedRows(List<List<String>> rows) {
        List<Provider> providers = rows.stream()
                .map(this::mapRowToProvider)
                .collect(Collectors.toList());

        return providerRepository.saveAll(providers)
                .collectList()
                .map(saved -> ImportResponse.builder()
                        .success(true)
                        .message("Se importaron " + saved.size() + " proveedores exitosamente.")
                        .totalProcessed(rows.size())
                        .totalImported(saved.size())
                        .errors(new ArrayList<>())
                        .build());
    }
}
