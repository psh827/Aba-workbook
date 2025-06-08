package com.example.backend.repository.maintenance;

import com.example.backend.domain.maintenance.MaintenanceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceItemRepository extends JpaRepository<MaintenanceItem, Long> {
}
