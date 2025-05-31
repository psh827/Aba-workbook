package com.example.backend.domain.basic;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Leaders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leader {
    @Id
    @Column(length = 10)
    private String id;  // 담당자 고유 ID

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(length = 8)
    private String hireDate;

    @Column(length = 3, nullable = false)
    private String authCdNo;

    @Column(length = 10)
    private String childId;  // 관계로 바꿀 수도 있음

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
