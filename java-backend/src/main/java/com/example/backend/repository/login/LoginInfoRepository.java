package com.example.backend.repository.login;

import com.example.backend.domain.common.LoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginInfoRepository extends JpaRepository<LoginInfo, String> {
    Optional<LoginInfo> findByLoginIdAndUseYn(String loginId, String useYn);
}
