package com.example.backend.domain.programs;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Program_items")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProgramItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;  // 항목 고유 ID

    @Column(length = 30, nullable = false)
    private String programId;  // 상위 프로그램 ID (Programs.program_id 참조)

    @Column(length = 100, nullable = false)
    private String itemName;  // 항목 이름

    @Column(length = 10)
    private String st = "0";  // 상태코드 (기본값 '0')

    @Column
    private Integer tsm = 0;  // 총 세션 중 행동 발생 수

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}