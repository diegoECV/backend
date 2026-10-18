package vallegrande.edu.pe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import vallegrande.edu.pe.backend.model.Order;
import vallegrande.edu.pe.backend.repository.OrderRepository;
import vallegrande.edu.pe.backend.util.ReportExportUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderReportService {

    private final OrderRepository orderRepository;

    private static final String[] HEADERS = {
        "ID Pedido", "Código Orden", "ID Cliente", "Fecha Pedido", "Incoterm", "Estado", "Moneda", "Condición Pago", "Puerto Destino", "Fecha Estimada Envío"
    };

    private List<String> mapOrderToRow(Order o) {
        return List.of(
            o.getOrderId() != null ? String.valueOf(o.getOrderId()) : "",
            o.getOrderCode() != null ? o.getOrderCode() : "",
            o.getClientId() != null ? o.getClientId() : "",
            o.getOrderDate() != null ? o.getOrderDate().toString() : "",
            o.getIncoterm() != null ? o.getIncoterm() : "",
            o.getStatus() != null ? o.getStatus() : "",
            o.getCurrency() != null ? o.getCurrency() : "",
            o.getPaymentTerms() != null ? o.getPaymentTerms() : "",
            o.getDestinationPort() != null ? o.getDestinationPort() : "",
            o.getEstimatedShippingDate() != null ? o.getEstimatedShippingDate().toString() : ""
        );
    }

    public Mono<byte[]> exportToExcel() {
        return orderRepository.findAll()
            .map(this::mapOrderToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateExcel("Pedidos", HEADERS, rows));
    }

    public Mono<byte[]> exportToPdf() {
        return orderRepository.findAll()
            .map(this::mapOrderToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generatePdf("Reporte de Pedidos de Exportación", HEADERS, rows));
    }

    public Mono<byte[]> exportToCsv() {
        return orderRepository.findAll()
            .map(this::mapOrderToRow)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .map(rows -> ReportExportUtil.generateCsv(HEADERS, rows));
    }
}
