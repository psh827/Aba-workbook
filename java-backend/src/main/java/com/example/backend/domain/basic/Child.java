package com.example.backend.domain.basic;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Child")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Child {

    @Id
    @Column(length = 10)
    private String id;  // 아동 고유 ID (기본키)

    @Column(length = 100, nullable = false)
    private String name;  // 아동 이름

    @Column
    private Integer age = 0;  // 나이 (기본값 0)

    @Column
    private String leaderId;  // 담당자 고유 ID (foreign key 후보)

    @Column(length = 10)
    private String momId;  // 엄마 고유 ID

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}