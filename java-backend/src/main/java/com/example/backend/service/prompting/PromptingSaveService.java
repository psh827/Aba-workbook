package com.example.backend.service.prompting;

import com.example.backend.domain.prompting.*;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.prompting.*;
import com.example.backend.utils.CommonUtils;
import com.example.backend.utils.StringUtils;
import com.example.backend.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromptingSaveService {
    private final PromptingHierarchyRepository promptingHierarchyRepository;
    private final PromptingItemRepository promptingItemRepository;
    private final PromptingNoteRepository promptingNoteRepository;
    private final PromptingNote2Repository promptingNote2Repository;
    private final PromptingStepRepository promptingStepRepository;

    public void save(UploadRequest uploadData, UploadSharedInfo sharedInfo) {
        String weekCnt = sharedInfo.getWeekCnt();
        String childId = sharedInfo.getChildId();
        List<Map<String, Object>>  promptingHierarchy =(List<Map<String, Object>> ) uploadData.getPrograms_result().get("prompting_hierarchy");
        List<Map<String, Object>> promptingHierarchy2 = (List<Map<String, Object>>) uploadData.getPrograms_result().get("prompting_hierarchy2");
        List<Map<String, Object>> activities = null;
        Map<String, Object> promptingRatios = null;
        Map<String, Object> firstRatios = null;
        Map<String, Object> secondRatios = null;
        for(int i = 0; i < promptingHierarchy.size(); i++){
            Map<String, Object> promptingItems = (Map<String, Object>) promptingHierarchy.get(i);
            if(!CommonUtils.isNullOrBlank(promptingItems) && !CommonUtils.isNullOrBlank(promptingItems.get("category"))){
                List<Map<String, Object>> ratios = new ArrayList<>();
                String hierarchyId = weekCnt + childId + "1" + StringUtils.formatTwoDigits(i+1);
                promptingRatios = (Map<String, Object>) promptingItems.get("ratios");
                firstRatios = (Map<String, Object>) promptingRatios.get("first_ratio");
                secondRatios = (Map<String, Object>) promptingRatios.get("second_ratio");
                ratios.add(firstRatios);
                ratios.add(secondRatios);
                String tsmObj = secondRatios.get("tsm") != null ? secondRatios.get("tsm").toString() : "0";
                String sessionCntObj = secondRatios.get("session_cnt") != null ? secondRatios.get("session_cnt").toString() : "0";
                int tsm = CommonUtils.safeToInt(tsmObj);
                int sessionCnt = CommonUtils.safeToInt(sessionCntObj);
                PromptingHierarchy promptingHierarchyEntity = buildPromptingHierarchyEntity(hierarchyId, childId, weekCnt, promptingItems.get("category").toString(),
                        promptingItems.get("sub_category").toString(), tsm, sessionCnt);
                log.info("promptingHierarchyEntity : {}", promptingHierarchyEntity);
                promptingHierarchyRepository.save(promptingHierarchyEntity);
                // prompting_step start
                activities = (List<Map<String, Object>>) promptingItems.get("activities");
                savePromptingStepsAndNotes(hierarchyId, "1", activities);
                savePromptingItems(hierarchyId, childId, ratios);
            }
        }

        for(int i = 0; i < promptingHierarchy2.size(); i++){
            Map<String, Object> promptingItems = (Map<String, Object>) promptingHierarchy2.get(i);
            if(!CommonUtils.isNullOrBlank(promptingItems) && !CommonUtils.isNullOrBlank(promptingItems.get("category"))){
                List<Map<String, Object>> ratios2 = new ArrayList<>();
                String hierarchyId = weekCnt + childId + "2" + StringUtils.formatThreeDigits(i+1);
                promptingRatios = (Map<String, Object>) promptingItems.get("ratios");
                firstRatios = (Map<String, Object>) promptingRatios.get("first_ratio");
                secondRatios = (Map<String, Object>) promptingRatios.get("second_ratio");
                ratios2.add(firstRatios);
                ratios2.add(secondRatios);
                String tsmObj = secondRatios.get("tsm") != null ? secondRatios.get("tsm").toString() : "0";
                String sessionCntObj = secondRatios.get("session_cnt") != null ? secondRatios.get("session_cnt").toString() : "0";
                int tsm = CommonUtils.safeToInt(tsmObj);
                int sessionCnt = CommonUtils.safeToInt(sessionCntObj);
                String sub_category = promptingItems.get("sub_category") != null ? promptingItems.get("sub_category").toString() : "";
                PromptingHierarchy promptingHierarchyEntity = buildPromptingHierarchyEntity(hierarchyId, childId, weekCnt, promptingItems.get("category").toString(),
                        sub_category, tsm, sessionCnt);
                log.info("promptingHierarchyEntity2 : {}", promptingHierarchyEntity);
                promptingHierarchyRepository.save(promptingHierarchyEntity);
                activities = (List<Map<String, Object>>) promptingItems.get("activities");
                savePromptingStepsAndNotes(hierarchyId, "2", activities);
                savePromptingItems(hierarchyId, childId, ratios2);

            }
        }
    }



    public PromptingHierarchy buildPromptingHierarchyEntity(
            String hierarchyId,
            String childId,
            String weekCnt,
            String category,
            String subCategory,
            int sessionCnt,
            int tsm
    ) {
        return PromptingHierarchy.builder()
                .hierarchyId(hierarchyId)
                .childId(childId)
                .weekCnt(weekCnt)
                .category(category)
                .subCategory(subCategory)
                .totalSessions(tsm)
                .sittingCount(sessionCnt)
                .build();
    }

    public void savePromptingStepsAndNotes(
            String hierarchyId,
            String hierarchyType,
            List<Map<String, Object>> activities
    ) {
        for (int i = 0; i < activities.size(); i++) {
            Map<String, Object> item = activities.get(i);
            if (!CommonUtils.isNullOrBlank(item) && !CommonUtils.isNullOrBlank(item.get("step")) && hierarchyType.equals("1")) {
                PromptingStep stepEntity = PromptingStep.builder()
                        .hierarchyId(hierarchyId)
                        .seq(i+1)
                        .step(item.get("step").toString())
                        .hip(StringUtils.nvl((String) item.get("hip"), "0"))
                        .ind(StringUtils.nvl((String) item.get("ind"), "0"))
                        .build();

                log.info("promptingStepEntity : {}", stepEntity);

                Long itemId = promptingStepRepository.save(stepEntity).getStepId();

                if (!CommonUtils.isNullOrBlank(item.get("note"))) {
                    PromptingNote noteEntity = PromptingNote.builder()
                            .hierarchyId(hierarchyId)
                            .itemId(itemId)
                            .note(item.get("note").toString())
                            .build();

                    log.info("promptingNoteEntity : {}", noteEntity);
                    promptingNoteRepository.save(noteEntity);
                }
            }else{
                if(!CommonUtils.isNullOrBlank(item) && !CommonUtils.isNullOrBlank(item.get("note")) && hierarchyType.equals("2")){
                    Object hipObj = item.get("hip");
                    if(hipObj != null && !CommonUtils.isNullOrBlank(item.get("hip").toString()) && !"hip".equals(item.get("hip").toString())){
                        String step = item.get("step") != null ? item.get("step").toString() : "";
                        PromptingStep stepEntity = PromptingStep.builder()
                                .hierarchyId(hierarchyId)
                                .step(step)
                                .hip(StringUtils.nvl((String) item.get("hip"), "0"))
                                .ind(StringUtils.nvl((String) item.get("ind"), "0"))
                                .seq(i+1)
                                .build();
                        log.info("promptingStepEntity2 : {}", stepEntity);
                        promptingStepRepository.save(stepEntity);
                    }
                    PromptingNote2 noteEntity = null;
                    noteEntity = PromptingNote2.builder()
                            .hierarchyId(hierarchyId)
                            .note(item.get("note").toString())
                            .build();
                    promptingNote2Repository.save(noteEntity);
                    Object noteObj = item.get("note2");
                    if(noteObj != null && CommonUtils.isNullOrBlank(item.get("note2").toString())){
                        noteEntity = PromptingNote2.builder()
                                .hierarchyId(hierarchyId)
                                .note(item.get("note").toString())
                                .build();
                        promptingNote2Repository.save(noteEntity);
                    }
                }
            }
        }
    }

    public void savePromptingItems(
            String hierarchyId,
            String childId,
            List<Map<String, Object>> ratios
    ) {
        String[] keyCheck = {"text", "success_cnt", "attempt_cnt"};
        for (int j = 0; j < ratios.size(); j++) {
            Map<String, Object> ratio = ratios.get(j);

            if (!CommonUtils.isNullOrBlank(ratio)
                   && !ValidationUtils.isAllRatioFieldsEmpty(ratio, keyCheck)
            ) {
                log.info("debug! text : {}" + CommonUtils.safeToString(ratio.get("text")));
                log.info("debug! text : {}" + ratio.get("text"));
               PromptingItem promptingItemEntity = PromptingItem.builder()
                       .hierarchyId(hierarchyId)
                       .childId(childId)
                       .itemName(StringUtils.nvl(
                               CommonUtils.safeToString(ratio.get("text")), "-"))
                       .itemSucCnt(CommonUtils.safeToInt(
                               StringUtils.nvl(CommonUtils.safeToString(ratio.get("success_cnt")), "0")))
                       .itemTotalCnt(CommonUtils.safeToInt(
                               StringUtils.nvl(CommonUtils.safeToString(ratio.get("attempt_cnt")), "0")))
                       .build();
                    log.info("promptingItemEntity : {}", promptingItemEntity);
                    promptingItemRepository.save(promptingItemEntity);
                }

            }
        }
}
