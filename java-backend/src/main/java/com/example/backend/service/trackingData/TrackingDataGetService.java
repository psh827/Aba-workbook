package com.example.backend.service.trackingData;

import com.example.backend.domain.trackingData.TrackingData;
import com.example.backend.repository.tracking.TrackingDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingDataGetService {
    private final TrackingDataRepository trackingDataRepository;

    public List<String> getBehaviorByChildId(String childId){
        return trackingDataRepository.findDistinctBehaviorsByChildId(childId);
    }
}
