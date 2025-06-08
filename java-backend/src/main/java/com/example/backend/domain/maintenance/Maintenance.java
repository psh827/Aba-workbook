package com.example.backend.domain.maintenance;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "Maintenance")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maintenance {
    @Id
    @Column(name = "maintenance_id", length = 30, nullable = false)
    private String maintenanceId;

    @Column(name = "child_id", length = 10, nullable = false)
    private String childId;

    @Column(name = "week_cnt", length = 10, nullable = false)
    private String weekCnt;

    @Column(name = "maintenance_text", length = 300, nullable = false)
    private String maintenanceText;

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}
