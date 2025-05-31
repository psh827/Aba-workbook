package com.example.backend.domain.programs;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Program_notes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProgramNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;  // 노트 고유 ID

    @Column(length = 30, nullable = false)
    private String programId;  // 상위 프로그램 ID (Programs.program_id 참조)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String note;  // 자유 메모 텍스트 내용

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}