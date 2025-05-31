package com.example.backend.repository.prompting;

import com.example.backend.domain.prompting.PromptingStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptingStepRepository extends JpaRepository<PromptingStep, Long> {
}
