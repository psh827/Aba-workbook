package com.example.backend.domain.reinforcement;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reinforcement_notes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReinforcementNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;  // 노트 고유 ID

    @Column(length = 30, nullable = false)
    private String samplingId;  // 상위 강화 샘플 ID (reinforcement_samplings.sampling_id 참조)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String note;  // 자유 메모 내용

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}