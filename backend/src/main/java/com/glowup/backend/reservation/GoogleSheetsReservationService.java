package com.glowup.backend.reservation;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class GoogleSheetsReservationService {
    private static final DateTimeFormatter CREATED_AT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final String spreadsheetId;
    private final String credentialsJson;

    public GoogleSheetsReservationService(
        @Value("${google.sheets.spreadsheet-id:}") String spreadsheetId,
        @Value("${google.sheets.credentials-json:}") String credentialsJson
    ) {
        this.spreadsheetId = spreadsheetId;
        this.credentialsJson = credentialsJson;
    }

    public void append(Reservation reservation) {
        if (spreadsheetId.isBlank() || credentialsJson.isBlank()) return;
        try {
            GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8)))
                .createScoped(List.of(SheetsScopes.SPREADSHEETS));
            Sheets sheets = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
            ).setApplicationName("Glow Up").build();

            String tab = reservation.getServiceType() == ServiceType.NAILS
                ? "Servicios de uñas"
                : "Tratamientos capilares";
            ValueRange values = new ValueRange().setValues(List.of(List.of(
                reservation.getId(),
                reservation.getClientName(),
                reservation.getClientPhone(),
                reservation.getServiceName(),
                reservation.getDate().toString(),
                reservation.getTime(),
                reservation.getCreatedAt().format(CREATED_AT_FORMAT)
            )));
            sheets.spreadsheets().values()
                .append(spreadsheetId, tab + "!A:G", values)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo sincronizar la reserva con Google Sheets", exception);
        }
    }
}
