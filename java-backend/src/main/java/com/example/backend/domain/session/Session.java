package com.example.backend.domain.session;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Sessions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(SessionId.class)
public class Session {

    @Id
    @Column(length = 10)
    private String weekCnt;  // 세션 주

    @Id
    @Column(length = 10)
    private String childId;  // 아동 ID

    @Column
    private Integer homeSessions = 0;

    @Column
    private Integer trainingSessions = 0;

    @Column
    private Integer schoolSessions = 0;

    @Column
    private Integer communityTrips = 0;

    @Column
    private Integer schoolTrainingSessions = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}