package com.example.backend.domain.programs;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Programs")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Program {

    @Id
    @Column(length = 30)
    private String programId;  // 프로그램 고유 ID (week_cnt + child_id + 증가번호)

    @Column(length = 10, nullable = false)
    private String weekCnt;  // 주차 정보

    @Column(length = 10, nullable = false)
    private String childId;  // 아동 ID (Child.id 참조)

    @Column(length = 100, nullable = false)
    private String programName;  // 프로그램 이름

    @Column
    private Integer sessionCnt = 0;  // 전체 세션 수

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}