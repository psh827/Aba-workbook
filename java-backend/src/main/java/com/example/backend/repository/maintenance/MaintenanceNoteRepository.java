package com.example.backend.repository.maintenance;

import com.example.backend.domain.maintenance.MaintenanceNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceNoteRepository extends JpaRepository<MaintenanceNote,Long> {
}
