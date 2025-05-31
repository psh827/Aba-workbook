package com.example.backend.repository.tracking;

import com.example.backend.domain.trackingData.TrackingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingDataRepository extends JpaRepository<TrackingData, Long> {
}
