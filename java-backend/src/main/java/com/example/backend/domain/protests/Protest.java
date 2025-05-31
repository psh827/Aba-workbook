package com.example.backend.domain.protests;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Protests")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Protest {

    @Id
    @Column(length = 30)
    private String protestId;  // 반항 고유 ID (week_cnt + child_id + 증가번호)

    @Column(length = 10, nullable = false)
    private String childId;  // 아동 ID

    @Column(length = 10, nullable = false)
    private String weekCnt;  // 주차 정보

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private ProtestType protestType = ProtestType.SHORT;  // 항의 유형 (ENUM)

    @Column(length = 8)
    private String protestDate;  // 발생 일시 (예: 20250503)

    @Column(length = 10)
    private String protestTime;  // 발생 시간

    @Column(length = 100)
    private String protestLocation;  // 장소 정보

    @Column(length = 50)
    private String staffName;  // 담당자 이름

    @Column(columnDefinition = "TEXT")
    private String preCondition;  // 발생 전 상황 설명

    @Column
    private String durationMin;  // 행동 지속 시간(분)

    @Column
    private String durationSec;  // 행동 지속 시간(초)

    @Column(length = 5)
    private String functionTypeCd;  // 기능 분석 유형 코드

    @Column(length = 1000)
    private String consequences;  // 대응 결과 요약

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}