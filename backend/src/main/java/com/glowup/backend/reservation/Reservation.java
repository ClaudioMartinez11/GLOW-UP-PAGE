package com.glowup.backend.reservation;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;
    @Column(nullable = false)
    private String serviceName;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private String time;
    @Column(nullable = false)
    private String clientName;
    @Column(nullable = false)
    private String clientPhone;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void setCreatedAt() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getClientPhone() { return clientPhone; }
    public void setClientPhone(String clientPhone) { this.clientPhone = clientPhone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
