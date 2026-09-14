package com.glowup.backend.reservation;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ServiceType {
    NAILS,
    HAIR;

    @JsonCreator
    public static ServiceType fromValue(String value) {
        return value == null ? null : valueOf(value.trim().toUpperCase());
    }
}
