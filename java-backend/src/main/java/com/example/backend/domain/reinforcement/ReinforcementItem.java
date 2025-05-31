package com.example.backend.domain.reinforcement;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reinforcement_items", schema = "aba_db")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReinforcementItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;  // item 고유 ID

    @Column(name = "sampling_id", nullable = false, length = 30)
    private String samplingId;  // 강화 샘플 고유 ID

    @Column(name = "week_cnt", nullable = false, length = 10)
    private String weekCnt;  // 주차 정보 (예: 2501-1)

    @Column(name = "item_text", length = 50)
    private String itemText;  // 샘플링 텍스트

    @Column(name = "item_suc_cnt", columnDefinition = "int default 0")
    private Integer itemSucCnt;  // 샘플링 성공 횟수

    @Column(name = "item_attempt_cnt", columnDefinition = "int default 0")
    private Integer itemAttemptCnt;  // 샘플링 시도 횟수

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각

    // 연관관계 매핑 (옵션) - 외래키를 활용한 객체 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sampling_id", insertable = false, updatable = false)
    private ReinforcementSampling reinforcementSampling;
}
