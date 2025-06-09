package com.example.backend.controller.view;

import com.example.backend.service.trackingData.TrackingDataGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/view")
@RequiredArgsConstructor
public class ViewController {

    private final TrackingDataGetService trackingDataGetService;

    @GetMapping("/tracking")
    public ResponseEntity<?> getTrackingBehavior(@RequestParam String childId){

        List<String> behaviors = trackingDataGetService.getBehaviorByChildId(childId);

        return ResponseEntity.ok(behaviors);
    }


}
