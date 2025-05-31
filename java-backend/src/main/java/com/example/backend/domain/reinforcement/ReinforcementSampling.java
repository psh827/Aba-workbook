package com.example.backend.domain.reinforcement;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reinforcement_samplings")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReinforcementSampling {

    @Id
    @Column(length = 30)
    private String samplingId;  // 강화 샘플 고유 ID (week_cnt + child_id + 증가번호)

    @Column(length = 10, nullable = false)
    private String childId;  // 아동 ID (Child.id 참조)

    @Column(length = 10, nullable = false)
    private String weekCnt;  // 주차 정보

    @Column(length = 100, nullable = false)
    private String title;  // 강화 종류 (예: 음식, 활동 등)

    @Column(length = 100, nullable = false)
    private String detailSampling;  // 강화 항목 내용

    @Column
    private Integer sessionCnt = 0;

    @Column
    private Integer tsm = 0;

    @Column
    private Integer MI = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}
