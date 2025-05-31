package com.example.backend.domain.prompting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Prompting_notes2")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptingNote2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;  // 메모 고유 ID

    @Column(length = 30, nullable = false)
    private String hierarchyId;  // 상위 촉구 프로그램 ID


    @Column(columnDefinition = "TEXT", nullable = false)
    private String note;  // 자유 메모 내용

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}