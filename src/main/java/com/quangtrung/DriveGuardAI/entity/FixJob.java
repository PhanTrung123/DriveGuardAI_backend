package com.quangtrung.DriveGuardAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "fix_jobs")
@Data
public class FixJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fix_job_id")
    private Long fixJobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id", nullable = false)
    private User mechanic;

    @Column(name = "job_note", columnDefinition = "TEXT")
    private String jobNote;

    @Column(nullable = false, length = 50)
    private String status; // ASSIGNED, REPAIRING, DONE

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
