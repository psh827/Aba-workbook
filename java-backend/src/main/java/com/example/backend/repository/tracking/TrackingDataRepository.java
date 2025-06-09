package com.example.backend.repository.tracking;

import com.example.backend.domain.trackingData.TrackingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrackingDataRepository extends JpaRepository<TrackingData, Long> {
    @Query("SELECT DISTINCT td.behavior FROM TrackingData td WHERE td.childId = :childId ORDER BY td.behavior")
    List<String> findDistinctBehaviorsByChildId(@Param("childId") String childId);
}
