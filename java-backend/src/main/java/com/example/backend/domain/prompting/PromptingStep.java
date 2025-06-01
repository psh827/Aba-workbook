package com.example.backend.domain.prompting;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Prompting_steps")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PromptingStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;  // 촉구 단계 고유 ID

    @Column(length = 20, nullable = false)
    private String hierarchyId;  // 상위 촉구 프로그램 ID

    @Column(nullable = false)
    private Integer seq;  // 단계 순서

    @Column(length = 50, nullable = false)
    private String step;  // 단계 이름 또는 번호

    @Column(length = 10)
    private String ind;  // 독립 수행 여부

    @Column(length = 10)
    private String hip;  // 신체적 도움 여부

    @Column(columnDefinition = "TEXT")
    private String note;  // 코멘트 또는 기록

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}