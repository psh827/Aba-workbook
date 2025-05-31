package com.example.backend.domain.common;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Login_Info")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginInfo {

    @Id
    @Column(length = 50)
    private String loginId;  // 로그인 아이디 (PK)

    @Column(length = 100, nullable = false)
    private String loginPw;  // 암호화된 비밀번호

    @Column(length = 4, nullable = false)
    private String loginType;  // 사용자 유형 (TH, MM, SP, AD)

    @Column(length = 30, nullable = false)
    private String constraintKey;  // 각 사용자 테이블의 ID (리더, 엄마 등)

    @Column(length = 1, nullable = false)
    private String useYn = "Y";  // 사용 여부 (기본값 Y)

    @CreationTimestamp
    private LocalDateTime createdAt;  // 등록 시각

    @UpdateTimestamp
    private LocalDateTime updatedAt;  // 수정 시각
}