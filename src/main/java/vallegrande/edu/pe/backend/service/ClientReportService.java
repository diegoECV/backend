package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import vallegrande.edu.pe.backend.model.Client;
import vallegrande.edu.pe.backend.repository.ClientRepository;
import vallegrande.edu.pe.backend.util.ReportExportUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientReportService {

    private final ClientRepository clientRepository;

    private static final String[] HEADERS = {
        "ID", "Tipo Doc.", "Nro. Documento", "Empresa", "Contacto", "País", "Email", "Teléfono", "Incoterm", "Moneda"
    };

    private List<String> mapClientToRow(Client c) {
        return List.of(
            c.getId() != null ? c.getId() : "",
            c.getDocumentType() != null ? c.getDocumentType() : "",
            c.getDocumentNumber() != null ? c.getDocumentNumber() : "",
            c.getCompanyName() != null ? c.getCompanyName() : "",
            c.getContactName() != null ? c.getContactName() : "",
            c.getCountry() != null ? c.getCountry() : "",
            c.getEmail() != null ? c.getEmail() : "",
            c.getPhone() != null ? c.getPhone() : "",
            c.getPreferredIncoterm() != null ? c.getPreferredIncoterm() : "",
            c.getPreferredCurrency() != null ? c.getPreferredCurrency() : ""
        );
    }

    public Mono<byte[]> exportToExcel() {
        return clientRepository.findAll()
            .map(this::mapClientToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateExcel("Clientes", HEADERS, rows));
    }

    public Mono<byte[]> exportToPdf() {
        return clientRepository.findAll()
            .map(this::mapClientToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generatePdf("Reporte General de Clientes", HEADERS, rows));
    }

    public Mono<byte[]> exportToCsv() {
        return clientRepository.findAll()
            .map(this::mapClientToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateCsv(HEADERS, rows));
    }
}
