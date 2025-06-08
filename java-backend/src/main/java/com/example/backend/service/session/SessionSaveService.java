package com.example.backend.service.session;

import com.example.backend.domain.session.Session;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.session.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionSaveService {

    private final SessionRepository sessionRepository;

    public void saveSession(UploadRequest uploadData, UploadSharedInfo sharedInfo) {
        Map<String, Object> treatmentInformation = (Map<String, Object>) uploadData.getBx_info_results().get("treatment_information");
        String weekCnt = sharedInfo.getWeekCnt();
        String childId = sharedInfo.getChildId();

        Session session = buildSessionEntity(childId, weekCnt, treatmentInformation);

        log.info("######## session info ##########");
        log.info("session Entity : {}", session);
        sessionRepository.save(session);
        log.info("session saved!");
    }

    public Session buildSessionEntity(String childId, String weekCnt, Map<String, Object> treatmentInformation) {
        int homeSessionCntInt = parseCount(treatmentInformation, "home_sessions");
        int schoolSessionCntInt = parseCount(treatmentInformation, "school_sessions");
        int communityTripsCntInt = parseCount(treatmentInformation, "community_trips");
        int trainingSessionCntInt = parseCount(treatmentInformation, "training_sessions");
        int schoolTrainingSessionCntInt = parseCount(treatmentInformation, "school_training_sessions");

        return Session.builder()
                .childId(childId)
                .weekCnt(weekCnt)
                .homeSessions(homeSessionCntInt)
                .schoolSessions(schoolSessionCntInt)
                .trainingSessions(trainingSessionCntInt)
                .communityTrips(communityTripsCntInt)
                .schoolTrainingSessions(schoolTrainingSessionCntInt)
                .build();
    }

    private int parseCount(Map<String, Object> treatmentInfo, String key) {
        Map<String, Object> section = (Map<String, Object>) treatmentInfo.get(key);
        Object countObj = section != null ? section.get("count") : null;

        try {
            return (countObj != null) ? Integer.parseInt(countObj.toString().trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
