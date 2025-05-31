package com.example.backend.repository.protests;

import com.example.backend.domain.protests.Protest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProtestRepository extends JpaRepository<Protest, String> {
}
