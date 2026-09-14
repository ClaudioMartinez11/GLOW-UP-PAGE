package com.glowup.backend.reservation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository repository;
    private final GoogleSheetsReservationService googleSheets;

    public ReservationService(ReservationRepository repository, GoogleSheetsReservationService googleSheets) {
        this.repository = repository;
        this.googleSheets = googleSheets;
    }

    public Reservation create(ReservationRequest request) {
        Reservation reservation = new Reservation();
        reservation.setServiceType(request.serviceType());
        reservation.setServiceName(request.serviceName());
        reservation.setDate(request.date());
        reservation.setTime(request.time());
        reservation.setClientName(request.clientName());
        reservation.setClientPhone(request.clientPhone());
        Reservation saved = repository.save(reservation);
        googleSheets.append(saved);
        return saved;
    }

    public byte[] exportExcel() throws IOException {
        List<Reservation> reservations = repository.findAllByOrderByDateAscTimeAsc();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            createSheet(workbook, "Servicios de uñas", reservations, ServiceType.NAILS);
            createSheet(workbook, "Tratamientos capilares", reservations, ServiceType.HAIR);
            workbook.write(output);
            return output.toByteArray();
        }
    }

    private void createSheet(Workbook workbook, String name, List<Reservation> reservations, ServiceType type) {
        Sheet sheet = workbook.createSheet(name);
        Row header = sheet.createRow(0);
        String[] columns = {"ID", "Cliente", "Telefono", "Servicio", "Fecha", "Hora", "Creada"};
        for (int index = 0; index < columns.length; index++) {
            Cell cell = header.createCell(index);
            cell.setCellValue(columns[index]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        int rowIndex = 1;
        for (Reservation reservation : reservations) {
            if (reservation.getServiceType() != type) continue;
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(reservation.getId());
            row.createCell(1).setCellValue(reservation.getClientName());
            row.createCell(2).setCellValue(reservation.getClientPhone());
            row.createCell(3).setCellValue(reservation.getServiceName());
            row.createCell(4).setCellValue(reservation.getDate().toString());
            row.createCell(5).setCellValue(reservation.getTime());
            row.createCell(6).setCellValue(reservation.getCreatedAt().toString());
        }
        for (int index = 0; index < columns.length; index++) sheet.autoSizeColumn(index);
    }
}
