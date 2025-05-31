package com.example.backend.repository.prompting;

import com.example.backend.domain.prompting.PromptingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptingItemRepository extends JpaRepository<PromptingItem, Long> {
}
