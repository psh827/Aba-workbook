package com.example.backend.repository.reinforcement;

import com.example.backend.domain.reinforcement.ReinforcementItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReinforcementItemRepository extends JpaRepository<ReinforcementItem, Long> {
}
