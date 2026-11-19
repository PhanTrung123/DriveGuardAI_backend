package com.quangtrung.DriveGuardAI.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Data
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(name = "service_fee", precision = 15, scale = 2)
    private BigDecimal serviceFee;

    @Column(name = "total_money", precision = 15, scale = 2)
    private BigDecimal totalMoney;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @Column(name = "warranty_date")
    private LocalDate warrantyDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
