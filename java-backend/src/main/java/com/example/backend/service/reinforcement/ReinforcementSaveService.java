package com.example.backend.service.reinforcement;

import com.example.backend.domain.reinforcement.ReinforcementItem;
import com.example.backend.domain.reinforcement.ReinforcementNote;
import com.example.backend.domain.reinforcement.ReinforcementSampling;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.reinforcement.ReinforcementItemRepository;
import com.example.backend.repository.reinforcement.ReinforcementNoteRepository;
import com.example.backend.repository.reinforcement.ReinforcementSamplingRepository;
import com.example.backend.utils.CommonUtils;
import com.example.backend.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReinforcementSaveService {
    private final ReinforcementSamplingRepository reinforcementSamplingRepository;
    private final ReinforcementItemRepository reinforcementItemRepository;
    private final ReinforcementNoteRepository reinforcementNoteRepository;

    public void save(UploadRequest uploadData, UploadSharedInfo sharedInfo){
        List<Map<String, Object>> reinforcementSamplings = (List<Map<String, Object>>) uploadData.getPrograms_result().get("reinforcement_samplings");
        String weekCnt = sharedInfo.getWeekCnt();
        String childId = sharedInfo.getChildId();
        String detailSampling = "";
        String title = "";
        String samplingId = "";
        Map<String, Object> first_row = null;
        Map<String, Object> second_row = null;
        Map<String, Object> sampling_info = null;
        for(int i = 0; i < reinforcementSamplings.size(); i++){
            Map<String, Object> sampling = (Map<String, Object>) reinforcementSamplings.get(i);
            if(!CommonUtils.isNullOrBlank(sampling) && !CommonUtils.isNullOrBlank(sampling.get("title"))){
                samplingId = weekCnt + childId + "1" + StringUtils.formatTwoDigits(i+1);
                detailSampling = sampling.get("detail_sampling") != null ? sampling.get("detail_sampling").toString() : "-";
                title = sampling.get("title") != null ? sampling.get("title").toString() : "-";
                first_row = (Map<String, Object>) sampling.get("first_row");
                second_row = (Map<String, Object>) sampling.get("second_row");
                sampling_info = (Map<String, Object>) sampling.get("sampling_info");
                saveReinforcementSampling(samplingId, childId, weekCnt, detailSampling, title, sampling, first_row, sampling_info);
                saveReinforcementSampling(samplingId, childId, weekCnt, detailSampling, title, sampling, second_row, sampling_info);
            }
        }
    }

    public void saveReinforcementSampling(
            String samplingId,
            String childId,
            String weekCnt,
            String detailSampling,
            String title,
            Map<String, Object> sampling,
            Map<String, Object> firstRow,
            Map<String, Object> sampling_info
    ) {
        // 강화 샘플링 엔티티 생성 및 저장
        ReinforcementSampling reinforcementSamplingEntity = ReinforcementSampling.builder()
                .samplingId(samplingId)
                .childId(childId)
                .weekCnt(weekCnt)
                .detailSampling(detailSampling)
                .title(title)
                .sessionCnt(CommonUtils.safeToInt(StringUtils.nvl(safeToString(sampling_info.get("session_cnt")), "0")))
                .tsm(CommonUtils.safeToInt(StringUtils.nvl(safeToString(sampling_info.get("tsm")), "0")))
                .MI(CommonUtils.safeToInt(StringUtils.nvl(safeToString(sampling_info.get("MI")), "0")))
                .build();
        log.info("reinforcementSamplingEntity : {}", reinforcementSamplingEntity);
        reinforcementSamplingRepository.save(reinforcementSamplingEntity);

        // 강화 아이템 및 노트 저장
        if (!CommonUtils.isNullOrBlank(firstRow)) {
            ReinforcementItem reinforcementItemEntity = ReinforcementItem.builder()
                    .samplingId(samplingId)
                    .weekCnt(weekCnt)
                    .itemText(StringUtils.nvl(safeToString(firstRow.get("ratios_text")), "-"))
                    .itemSucCnt(CommonUtils.safeToInt(StringUtils.nvl(safeToString(firstRow.get("ratios_suc")), "0")))
                    .itemAttemptCnt(CommonUtils.safeToInt(StringUtils.nvl(safeToString(firstRow.get("ratios_attempt")), "0")))
                    .build();
            log.info("reinforcementItemEntity : {}", reinforcementItemEntity);
            reinforcementItemRepository.save(reinforcementItemEntity);

            // note1 저장
            if (!CommonUtils.isNullOrBlank(firstRow.get("note1"))) {
                ReinforcementNote note1 = ReinforcementNote.builder()
                        .samplingId(samplingId)
                        .note(StringUtils.nvl(safeToString(firstRow.get("note1")), "-"))
                        .build();
                log.info("reinforcementNoteEntity1 : {}", note1);
                reinforcementNoteRepository.save(note1);
            }

            // note2 저장
            if (!CommonUtils.isNullOrBlank(firstRow.get("note2"))) {
                ReinforcementNote note2 = ReinforcementNote.builder()
                        .samplingId(samplingId)
                        .note(StringUtils.nvl(safeToString(firstRow.get("note2")), "-"))
                        .build();
                log.info("reinforcementNoteEntity2 : {}", note2);
                reinforcementNoteRepository.save(note2);
            }
        }
    }

    // 공통 문자열 캐스팅 함수
    private String safeToString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

}
