package com.example.backend.domain.treatmentInfomation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Treatment_information")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TreatmentInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long treatmentId;  // 치료 정보 고유 ID

    @Column(length = 10, nullable = false)
    private String childId;  // 아동 ID (Child.id 참조)

    @Column(length = 10, nullable = false)
    private String weekCnt;  // 주차 정보

    @Column(length = 50, nullable = false)
    private String staffName;  // 세션 담당자 이름

    @Column(nullable = false)
    private Integer sessionCnt = 0;  // 수행한 세션 횟수

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}