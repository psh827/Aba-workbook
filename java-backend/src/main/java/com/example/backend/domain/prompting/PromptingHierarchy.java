package com.example.backend.domain.prompting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Prompting_hierarchy")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PromptingHierarchy {

    @Id
    @Column(length = 30)
    private String hierarchyId;  // 프로그램 고유 ID (week_cnt + child_id + 증가번호)

    @Column(length = 10, nullable = false)
    private String weekCnt;  // 주차 정보

    @Column(length = 10, nullable = false)
    private String childId;  // 아동 ID (Child.id 참조)

    @Column(length = 100, nullable = false)
    private String category;  // 촉구 프로그램 이름

    @Column(length = 50)
    private String subCategory;  // 영역 또는 분류

    @Column
    private Integer totalSessions = 0;  // 총 세션 수

    @Column
    private Integer sittingCount = 0;  // 시팅 수

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}