package com.example.backend.repository.programs;

import com.example.backend.domain.programs.ProgramItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramItemRepository extends JpaRepository<ProgramItem, Long> {
}
