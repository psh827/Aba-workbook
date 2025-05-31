package com.example.backend.repository.protests;

import com.example.backend.domain.protests.ProtestsDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProtestsDetailRepository extends JpaRepository<ProtestsDetail, String> {
}
