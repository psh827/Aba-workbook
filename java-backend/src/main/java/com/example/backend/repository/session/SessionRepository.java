package com.example.backend.repository.session;

import com.example.backend.domain.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
