package com.example.backend.repository.reinforcement;

import com.example.backend.domain.reinforcement.ReinforcementNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReinforcementNoteRepository extends JpaRepository<ReinforcementNote, Long> {
}
