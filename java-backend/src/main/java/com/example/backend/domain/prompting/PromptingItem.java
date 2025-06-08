package com.example.backend.domain.prompting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Prompting_items")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PromptingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;  // 프로그램 항목 고유 ID

    @Column(length = 30, nullable = false)
    private String hierarchyId;  // 상위 촉구 프로그램 ID

    @Column(length = 10, nullable = false)
    private String childId;  // 아동 ID (Child.id 참조)

    @Column(length = 100, nullable = false)
    private String itemName;  // 아이템 명칭 (예: 실내)

    @Column
    private Integer itemSucCnt = 0;  // 성공 횟수 (기본값 0)

    @Column
    private Integer itemTotalCnt = 0;  // 총 시도 횟수 (기본값 0)

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}