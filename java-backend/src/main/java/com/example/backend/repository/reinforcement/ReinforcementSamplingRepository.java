package com.example.backend.repository.reinforcement;

import com.example.backend.domain.reinforcement.ReinforcementSampling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReinforcementSamplingRepository extends JpaRepository<ReinforcementSampling, Long> {
}
