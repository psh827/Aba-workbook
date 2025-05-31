package com.example.backend.repository.prompting;

import com.example.backend.domain.prompting.PromptingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptingNoteRepository extends JpaRepository<PromptingNote, Long> {
}
