package com.example.backend.repository.programs;

import com.example.backend.domain.programs.ProgramNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramNoteRepository extends JpaRepository<ProgramNote, Long> {
}
