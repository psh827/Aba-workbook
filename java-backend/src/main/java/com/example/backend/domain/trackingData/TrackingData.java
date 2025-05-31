package com.example.backend.domain.trackingData;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Tracking_data")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TrackingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 ID

    @Column(length = 10, nullable = false)
    private String childId;  // 아동 ID

    @Column(length = 10, nullable = false)
    private String weekCnt;  // 주차 정보

    @Column(length = 100, nullable = false)
    private String behavior;  // 행동 이름

    @Column
    private Integer behaviorTrials = 0;  // 행동 시도 횟수

    @Column
    private Integer trialsInSession;  // 세션 내 전체 시도 수

    @Column(length = 10)
    private String dataType;  // 수치 단위 (예: %, /ssn 등)

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}