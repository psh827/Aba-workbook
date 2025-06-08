package com.example.backend.repository.maintenance;

import com.example.backend.domain.maintenance.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance,Long> {
}
