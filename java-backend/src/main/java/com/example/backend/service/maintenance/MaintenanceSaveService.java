package com.example.backend.service.maintenance;

import com.example.backend.domain.maintenance.Maintenance;
import com.example.backend.domain.maintenance.MaintenanceItem;
import com.example.backend.domain.maintenance.MaintenanceNote;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.maintenance.MaintenanceItemRepository;
import com.example.backend.repository.maintenance.MaintenanceNoteRepository;
import com.example.backend.repository.maintenance.MaintenanceRepository;
import com.example.backend.utils.CommonUtils;
import com.example.backend.utils.StringUtils;
import com.example.backend.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceSaveService {
    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceItemRepository maintenanceItemRepository;
    private final MaintenanceNoteRepository maintenanceNoteRepository;

    public void save(UploadRequest uploadData, UploadSharedInfo sharedInfo){
        List<Map<String, Object>> maintenances = (List<Map<String, Object>>) uploadData.getMaintenance().get("maintenance");
        String childId = sharedInfo.getChildId();
        String weekCnt = sharedInfo.getWeekCnt();
        Map<String,Object> ratio = null;
        Map<String, Object> firstRatio = null;
        Map<String, Object> secondRatio = null;
        String[] keysToCheck = {
                "calculate", "note", "ratios_attempt", "ratios_suc", "ratios_text"
        };
        String ratiosText = "";
        String sucCnt = "";
        String attemptCnt = "";
        for(int i = 0; i<maintenances.size(); i++){
            Map<String, Object> maintenance = maintenances.get(i);
            String text = maintenance.get("item_text") != null ? (String) maintenance.get("item_text") : "";
            if(!CommonUtils.isNullOrBlank(maintenance) && !text.equals("")){
                ratio = maintenance.get("ratios") != null ? (Map<String, Object>) maintenance.get("ratios") : null;
                firstRatio = ratio.get("first") != null ? (Map<String, Object>) ratio.get("first") : null;
                secondRatio = ratio.get("second") != null ? (Map<String, Object>) ratio.get("second") : null;
                String maintenanceId = weekCnt + childId + "1" + StringUtils.formatThreeDigits(i + 1);
                Maintenance maintenanceEntity = Maintenance.builder()
                        .maintenanceId(maintenanceId)
                        .maintenanceText(text)
                        .childId(childId)
                        .weekCnt(weekCnt)
                        .build();
                maintenanceRepository.save(maintenanceEntity);

                if(!CommonUtils.isNullOrBlank(firstRatio) && !ValidationUtils.isAllRatioFieldsEmpty(firstRatio, keysToCheck)){
                    ratiosText = firstRatio.get("ratios_text") != null ? (String) firstRatio.get("ratios_text") : "";
                    sucCnt = firstRatio.get("ratios_suc") != null ? (String) firstRatio.get("ratios_suc") : "0";
                    attemptCnt = firstRatio.get("ratios_attempt") != null ? (String) firstRatio.get("ratios_attempt") : "0";
                    MaintenanceItem maintenanceItemEntity = MaintenanceItem.builder()
                            .maintenanceId(maintenanceId)
                            .itemText(ratiosText)
                            .itemSucCnt(CommonUtils.safeToInt(sucCnt))
                            .itemAttemptCnt(CommonUtils.safeToInt(attemptCnt))
                            .build();
                    maintenanceItemRepository.save(maintenanceItemEntity);
                    if(firstRatio.get("note") != null){
                        MaintenanceNote maintenanceNote = MaintenanceNote.builder()
                                .maintenanceId(maintenanceId)
                                .note(firstRatio.get("note").toString())
                                .build();
                        maintenanceNoteRepository.save(maintenanceNote);
                    }
                }
                if(!CommonUtils.isNullOrBlank(secondRatio) && !ValidationUtils.isAllRatioFieldsEmpty(secondRatio, keysToCheck)){
                    ratiosText = secondRatio.get("ratios_text") != null ? (String) secondRatio.get("ratios_text") : "";
                    sucCnt = secondRatio.get("ratios_suc") != null ? (String) secondRatio.get("ratios_suc") : "0";
                    attemptCnt = secondRatio.get("ratios_attempt") != null ? (String) secondRatio.get("ratios_attempt") : "0";
                    MaintenanceItem maintenanceItemEntity = MaintenanceItem.builder()
                            .maintenanceId(maintenanceId)
                            .itemText(ratiosText)
                            .itemSucCnt(CommonUtils.safeToInt(sucCnt))
                            .itemAttemptCnt(CommonUtils.safeToInt(attemptCnt))
                            .build();
                    maintenanceItemRepository.save(maintenanceItemEntity);
                    if(secondRatio.get("note") != null){
                        MaintenanceNote maintenanceNote = MaintenanceNote.builder()
                                .maintenanceId(maintenanceId)
                                .note(secondRatio.get("note").toString())
                                .build();
                        maintenanceNoteRepository.save(maintenanceNote);
                    }

                }
            }
        }

    }
}
