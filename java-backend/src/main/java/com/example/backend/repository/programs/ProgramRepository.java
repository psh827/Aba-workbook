package com.example.backend.repository.programs;

import com.example.backend.domain.programs.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
}
