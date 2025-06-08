package com.example.backend.domain.maintenance;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Maintenance_notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_id")
    private Long itemId;

    @Column(length = 30, nullable = false)
    private String maintenanceId;  // 상위 프로그램 ID (MaintenanceId.maintenance_id 참조)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String note;  // 자유 메모 텍스트 내용

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각

}
