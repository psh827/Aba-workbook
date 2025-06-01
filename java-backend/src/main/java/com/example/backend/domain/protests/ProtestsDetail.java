package com.example.backend.domain.protests;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Protests_detail")
@IdClass(ProtestsDetailId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProtestsDetail {

    @Id
    @Column(name = "protest_id", length = 30, nullable = false)
    private String protestId;

    @Id
    @Column(name = "behavior_name", length = 50, nullable = false)
    private String behaviorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "protest_type", nullable = false, columnDefinition = "ENUM('short', 'long') default 'short'")
    private ProtestType protestType;

    @Column(name = "behavior_cnt")
    private Integer behaviorCnt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
