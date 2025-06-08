package com.example.backend.domain.maintenance;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "Maintenance_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(length = 30, nullable = false)
    private String maintenanceId;  // 상위 프로그램 ID (MaintenanceId.maintenance_id 참조)

    @Column(length = 50)
    private String itemText;

    @Column
    private Integer itemSucCnt;

    @Column
    private Integer itemAttemptCnt;

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}
