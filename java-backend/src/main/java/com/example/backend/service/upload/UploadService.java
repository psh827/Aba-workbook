package com.example.backend.service.upload;

import com.example.backend.controller.upload.UploadController;
import com.example.backend.domain.basic.Child;
import com.example.backend.domain.programs.Program;
import com.example.backend.domain.programs.ProgramItem;
import com.example.backend.domain.programs.ProgramNote;
import com.example.backend.domain.prompting.*;
import com.example.backend.domain.protests.Protest;
import com.example.backend.domain.protests.ProtestType;
import com.example.backend.domain.protests.ProtestsDetail;
import com.example.backend.domain.reinforcement.ReinforcementItem;
import com.example.backend.domain.reinforcement.ReinforcementNote;
import com.example.backend.domain.reinforcement.ReinforcementSampling;
import com.example.backend.domain.session.Session;
import com.example.backend.domain.trackingData.TrackingData;
import com.example.backend.domain.treatmentInfomation.TreatmentInformation;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.programs.ProgramItemRepository;
import com.example.backend.repository.programs.ProgramNoteRepository;
import com.example.backend.repository.programs.ProgramRepository;
import com.example.backend.repository.prompting.*;
import com.example.backend.repository.protests.ProtestRepository;
import com.example.backend.repository.protests.ProtestsDetailRepository;
import com.example.backend.repository.reinforcement.ReinforcementItemRepository;
import com.example.backend.repository.reinforcement.ReinforcementNoteRepository;
import com.example.backend.repository.reinforcement.ReinforcementSamplingRepository;
import com.example.backend.repository.session.SessionRepository;
import com.example.backend.repository.tracking.TrackingDataRepository;
import com.example.backend.repository.treatment.TreatmentInformationRepository;
import com.example.backend.repository.user.ChildRepository;
import com.example.backend.service.maintenance.MaintenanceSaveService;
import com.example.backend.service.programs.ProgramSaveService;
import com.example.backend.service.prompting.PromptingSaveService;
import com.example.backend.service.protests.ProtestSaveService;
import com.example.backend.service.reinforcement.ReinforcementSaveService;
import com.example.backend.service.session.SessionSaveService;
import com.example.backend.service.trackingData.TrackingDataSaveService;
import com.example.backend.service.treatmentInformation.TreatmentInformationSaveService;
import com.example.backend.utils.CommonUtils;
import com.example.backend.utils.DateUtils;
import com.example.backend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;

@Service
@AllArgsConstructor
public class UploadService {
    private static final Logger log = LoggerFactory.getLogger(UploadService.class);
     /*** Programs ***/
    private final ProgramSaveService programSaveService;
     /*** prompting ***/
    private final PromptingSaveService promptingSaveService;
     /*** reinforcement ***/
    private final ReinforcementSaveService reinforcementSaveService;
     /*** session ***/
    private final SessionSaveService sessionSaveService;
     /*** trackingData ***/
    private final TrackingDataSaveService trackingDataSaveService;
     /*** treatment ***/
    private final TreatmentInformationSaveService treatmentInformationSaveService;
    private final ChildRepository childRepository;
    /*** protest ***/
    private final ProtestSaveService protestSaveService;
    /*** maintenance ***/
    private final MaintenanceSaveService maintenanceSaveService;
    @Transactional
    public String save(UploadRequest uploadData){

        try {
            /**** Data Set Start ****/
            Map<String, Object> basicInfo = (Map<String, Object>) uploadData.getBx_info_results().get("basic_info");
            /**** Data Set End ****/
            /**** Variable Start ****/
            String leaderName = basicInfo.get("leader_name").toString(); // 리더 이름
            String childName = basicInfo.get("name").toString().trim(); // 아동 이름
            String period = basicInfo.get("home_period").toString(); // Summary 작성 기간

            String weekCnt = DateUtils.getMonthlyWeekdayOrder(getWeekCnt(period), DayOfWeek.THURSDAY); // Summary 시작일 ex:250508
            log.info("써머리 YYMMIndex : {}", weekCnt);

            Child child = childRepository.findByName(childName)
                    .orElseThrow(() -> new RuntimeException("아이가 존재하지 않습니다. 아이 이름을 다시 확인해주세요."));
            log.info("child : {}", child);
            String childId = child.getId(); // 아동 ID DB저장용

            UploadSharedInfo sharedInfo = UploadSharedInfo.builder()
                    .weekCnt(weekCnt)
                    .childId(childId)
                    .childName(childName)
                    .leaderName(leaderName)
                    .build();

            /** SESSION DATA SET START **/
            log.info("session Data Start!");
            sessionSaveService.saveSession(uploadData, sharedInfo);
            log.info("session Data End!");
            /** SESSION DATA SET End **/
            /** Tracking Data Data Set Start **/
            log.info("tracking Data Start!");
            trackingDataSaveService.save(uploadData, sharedInfo);
            log.info("tracking Data End!");
            /** Tracking Data Data Set End **/
            /** Programs Data Set Start **/
            log.info("programs Data Start!");
            programSaveService.save(uploadData, sharedInfo);
            log.info("programs Data End!");
            /** Programs Data Set End **/
            /** Prompting Hierarchy Data Set Start **/
            log.info("prompting Hierarchy Data Start!");
            promptingSaveService.save(uploadData, sharedInfo);
            log.info("prompting Hierarchy Data End!");
            /**.prompting Hierarchy2 Data Set End **/
            /** reinforcement Samplings Data Set Start **/
            log.info("reinforcement Sampling Data Start!");
            reinforcementSaveService.save(uploadData, sharedInfo);
            log.info("reinforcement Sampling Data End!");
            /** reinforcement Samplings Data Set End **/
            /** protests Data Set Start **/
            log.info("Protests Data Start");
            protestSaveService.save(uploadData, sharedInfo);
            log.info("Protests Data End");
            /** protests Data Set End **/
            /** Treatment information Data Set Start **/
            log.info("treatment information Data Start!");
            treatmentInformationSaveService.save(uploadData, sharedInfo);
            log.info("treatment information Data End!");
            /** Treatment information Data Set End **/
            /** maintenance Data Set Start **/
            log.info("maintenance Data Start!");
            maintenanceSaveService.save(uploadData, sharedInfo);
            log.info("maintenance Data End!");
            /** maintenance Data Set End **/
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String getWeekCnt(String period){
        String[] weeks = period.split("~");
        String weekCnt = weeks[0].replaceAll("\\.", "");
        return weekCnt;
    }
}
