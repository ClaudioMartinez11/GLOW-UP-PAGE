package com.glowup.backend.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(
    @NotNull ServiceType serviceType,
    @NotBlank String serviceName,
    @NotNull LocalDate date,
    @NotBlank String time,
    @NotBlank String clientName,
    @NotBlank String clientPhone
) {}
