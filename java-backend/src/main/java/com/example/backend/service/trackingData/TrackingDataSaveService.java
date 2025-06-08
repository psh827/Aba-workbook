package com.example.backend.service.trackingData;

import com.example.backend.domain.trackingData.TrackingData;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.tracking.TrackingDataRepository;
import com.example.backend.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingDataSaveService {

    private final TrackingDataRepository trackingDataRepository;

    public void save(UploadRequest uploadData, UploadSharedInfo sharedInfo) {
        Map<String, Object> trackingData = (Map<String, Object>) uploadData.getBx_info_results().get("tracking_data");

        String weekCnt = sharedInfo.getWeekCnt();
        String childId = sharedInfo.getChildId();

        // ✅ behavior1 ~ behaviorN 저장
        for (int i = 0; i < trackingData.size(); i++) {
            Map<String, Object> behavior = (Map<String, Object>) trackingData.get("behavior" + (i + 1));
            if (!CommonUtils.isNullOrBlank(behavior) && !CommonUtils.isNullOrBlank(behavior.get("behavior"))) {
                TrackingData entity = TrackingData.builder()
                        .childId(childId)
                        .weekCnt(weekCnt)
                        .behavior(behavior.get("behavior").toString())
                        .behaviorTrials(CommonUtils.safeToInt(behavior.get("behavior_trials")))
                        .trialsInSession(CommonUtils.safeToInt(behavior.get("trials_in_session")))
                        .dataType(behavior.get("data_type").toString())
                        .build();

                trackingDataRepository.save(entity);
                log.info("✅ trackingData saved: {}", entity);
            }
        }

        // ✅ non-responses 별도 처리
        Map<String, Object> nonResponse = (Map<String, Object>) trackingData.get("non-responses");
        if (nonResponse != null) {
            TrackingData nonResponseEntity = TrackingData.builder()
                    .childId(sharedInfo.getChildId())
                    .weekCnt(sharedInfo.getWeekCnt())
                    .behavior("non-responses")
                    .behaviorTrials(CommonUtils.safeToInt(nonResponse.get("behavior_trials")))
                    .trialsInSession(CommonUtils.safeToInt(nonResponse.get("trials_in_session")))
                    .dataType(nonResponse.get("data_type").toString())
                    .build();

            trackingDataRepository.save(nonResponseEntity);
            log.info("✅ trackingData (non-response) saved: {}", nonResponseEntity);
        }

    }
}
