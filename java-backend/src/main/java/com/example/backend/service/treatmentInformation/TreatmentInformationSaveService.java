package com.example.backend.service.treatmentInformation;

import com.example.backend.domain.treatmentInfomation.TreatmentInformation;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.treatment.TreatmentInformationRepository;
import com.example.backend.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreatmentInformationSaveService {

    private final TreatmentInformationRepository treatmentInformationRepository;

    public void save(UploadRequest uploadData, UploadSharedInfo sharedInfo){
        Map<String, Object> treatmentInformation = (Map<String, Object>) uploadData.getBx_info_results().get("treatment_information");

        String weekCnt = sharedInfo.getWeekCnt();
        String childId = sharedInfo.getChildId();
        saveTreatmentInformation(childId, weekCnt, (List<Map<String, Object>>) treatmentInformation.get("staff"));
    }

    public void saveTreatmentInformation(
            String childId,
            String weekCnt,
            List<Map<String, Object>> staffInfoArray
    ) {
        TreatmentInformation treatmentInformationEntity = null;
        for(Map<String, Object> staffInfo : staffInfoArray){
            String th1 = staffInfo.get("th1") != null ? staffInfo.get("th1").toString() : "";
            String th2 = staffInfo.get("th2") != null ? staffInfo.get("th2").toString() : "";
            if(!CommonUtils.isNullOrBlank(th1)){
                treatmentInformationEntity = TreatmentInformation.builder()
                        .weekCnt(weekCnt)
                        .childId(childId)
                        .staffName(th1)
                        .sessionCnt(CommonUtils.safeToInt(staffInfo.get("th1_cnt").toString()))
                        .build();
                log.info("treatmentInformationEntity : {}", treatmentInformationEntity);
                treatmentInformationRepository.save(treatmentInformationEntity);
            }
            if(!CommonUtils.isNullOrBlank(th2)){
                treatmentInformationEntity = TreatmentInformation.builder()
                        .weekCnt(weekCnt)
                        .childId(childId)
                        .staffName(th2)
                        .sessionCnt(CommonUtils.safeToInt(staffInfo.get("th2_cnt").toString()))
                        .build();
                log.info("treatmentInformationEntity2 : {}", treatmentInformationEntity);
                treatmentInformationRepository.save(treatmentInformationEntity);
            }
        }
    }
}
