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
    private final ProgramRepository programRepository;
    private final ProgramItemRepository programItemRepository;
    private final ProgramNoteRepository programNoteRepository;
     /*** prompting ***/
    private final PromptingHierarchyRepository promptingHierarchyRepository;
    private final PromptingItemRepository promptingItemRepository;
    private final PromptingNoteRepository promptingNoteRepository;
    private final PromptingNote2Repository promptingNote2Repository;
    private final PromptingStepRepository promptingStepRepository;
     /*** reinforcement ***/
    private final ReinforcementSamplingRepository reinforcementSamplingRepository;
    private final ReinforcementItemRepository reinforcementItemRepository;
    private final ReinforcementNoteRepository  reinforcementNoteRepository;
     /*** session ***/
    private final SessionRepository sessionRepository;
     /*** trackingData ***/
    private final TrackingDataRepository trackingDataRepository;
     /*** treatment ***/
    private final TreatmentInformationRepository treatmentInformationRepository;
    private final ChildRepository childRepository;
    /*** protest ***/
    private final ProtestRepository protestRepository;
    private final ProtestsDetailRepository protestsDetailRepository;

    /**
     *
     * @param uploadData
     * @return String
     * <p><b>basic_info 필드 구조:</b></p>
     * <ul>
     *   <li><b>name</b>: 아이 이름</li>
     *   <li><b>leader_name</b>: 아이 리더 이름</li>
     *   <li><b>home_period</b>: Summary 작성 기간</li>
     * </ul>
     * <p><b>tracking_data 필드 구조:</b></p>
     * <ul>
     *   <li><b>[Array]behavior1-9</b>: 행동추적에 관한 배열객체 1번에서 9번까지 있음</li>
     *   <li><b>[Array]non-responses</b>: 비반응 객체</li>
     * </ul>
     * <p><b>tracking_data 각 객체에 포함된 필드 구조</b></p>
     * <ul>
     *     <li><b>behavior</b>: 행동</li>
     *
     * </ul>
     *
     */
    @Transactional
    public String save(UploadRequest uploadData){

        try {
            /**** Data Set Start ****/
            /** basic_info **/
            Map<String, Object> basicInfo = (Map<String, Object>) uploadData.getBx_info_results().get("basic_info");
            Map<String, Object> trackingData = (Map<String, Object>) uploadData.getBx_info_results().get("tracking_data");
            Map<String, Object> treatmentInformation = (Map<String, Object>) uploadData.getBx_info_results().get("treatment_information");
            /** programs_result **/
            Map<String, Object> programs = (Map<String, Object>) uploadData.getPrograms_result().get("programs");
            List<Map<String, Object>>  promptingHierarchy =(List<Map<String, Object>> ) uploadData.getPrograms_result().get("prompting_hierarchy");
            List<Map<String, Object>> promptingHierarchy2 = (List<Map<String, Object>>) uploadData.getPrograms_result().get("prompting_hierarchy2");
            List<Map<String, Object>> reinforcementSamplings = (List<Map<String, Object>>) uploadData.getPrograms_result().get("reinforcement_samplings");
            /** protests_result **/
            List<Map<String, Object>> longProtests = (List<Map<String, Object>>) uploadData.getProtests_results().get("long_protests");
            List<Map<String, Object>> shortProtests = (List<Map<String, Object>>) uploadData.getProtests_results().get("short_protests");
            /**** Data Set End ****/
            /**** Variable Start ****/
            String leaderName = basicInfo.get("leader_name").toString(); // 리더 이름
            String childName = basicInfo.get("name").toString().trim(); // 아동 이름
            String period = basicInfo.get("home_period").toString(); // Summary 작성 기간

            String weekCnt = getWeekCnt(period); // Summary 시작일 ex:250508
            log.info("getweekCnt : {}", DateUtils.getMonthlyWeekdayOrder(weekCnt, DayOfWeek.THURSDAY));

            Child child = childRepository.findByName(childName)
                    .orElseThrow(() -> new RuntimeException("아이가 존재하지 않습니다. 아이 이름을 다시 확인해주세요."));
            log.info("child : {}", child);
            String childId = child.getId(); // 아동 ID DB저장용

            /** SESSION DATA SET START **/
            Session session = buildSessionEntity(childId, weekCnt, treatmentInformation);
            /** SESSION DATA SET End **/
            log.info("######## session info ##########");
            log.info("session Entity : {}", session);
            sessionRepository.save(session);
            log.info("session saved!");
            /** Tracking Data Data Set Start **/
            for(int i = 0; i < trackingData.size(); i++){
                Map<String, Object> behavior = (Map<String, Object>) trackingData.get("behavior" + (i+1));
                if(!CommonUtils.isNullOrBlank(behavior) && !CommonUtils.isNullOrBlank(behavior.get("behavior"))){
                    TrackingData trackingDataEntity = TrackingData.builder()
                            .childId(childId)
                            .weekCnt(weekCnt)
                            .behavior(behavior.get("behavior").toString())
                            .behaviorTrials(Integer.parseInt(behavior.get("behavior_trials").toString()))
                            .trialsInSession(CommonUtils.safeToInt(behavior.get("trials_in_session")))
                            .dataType(behavior.get("data_type").toString())
                            .build();
                    log.info("trackingDataEntity : {}", trackingDataEntity);
                    trackingDataRepository.save(trackingDataEntity);
                    log.info("trackingData saved!");
                }

            }
            /* Tracking Data non Response */
            Map<String, Object> behavior = (Map<String, Object>) trackingData.get("non-responses");
            TrackingData trackingDataEntity = TrackingData.builder()
                    .childId(childId)
                    .weekCnt(weekCnt)
                    .behavior("non-responses")
                    .behaviorTrials(Integer.parseInt(behavior.get("behavior_trials").toString()))
                    .trialsInSession(CommonUtils.safeToInt(behavior.get("trials_in_session")))
                    .dataType(behavior.get("data_type").toString())
                    .build();
            log.info("non-responses trackingDataEntity : {}", trackingDataEntity);
            trackingDataRepository.save(trackingDataEntity);
            log.info("trackingData(non-response) saved!");
            /** Tracking Data Data Set End **/
            log.info("trackingData end!");
            /** Programs Data Set Start **/
            log.info("programs Data start!");
            String programId = "";
            List<Map<String, Object>>  programItems = null;
            List<String> programNotes = null;
            Program programEntity = null;
            ProgramItem programItemEntity = null;
            ProgramNote programNoteEntity = null;
            for(int i = 0; i < programs.size(); i++){
                Map<String, Object> program = (Map<String, Object>) programs.get("program" + (i+1));
                if(!CommonUtils.isNullOrBlank(program) && !CommonUtils.isNullOrBlank(program.get("program_name"))){
                    programId = weekCnt + childId + "1" + StringUtils.formatThreeDigits(i+1);
                    programEntity = Program.builder()
                            .programId(programId)
                            .childId(childId)
                            .weekCnt(weekCnt)
                            .programName(program.get("program_name").toString())
                            .sessionCnt(CommonUtils.safeToInt(program.get("session_cnt")))
                            .build();
                    log.info("programEntity : {}", programEntity);
                    programRepository.save(programEntity);
                    log.info("program saved!");
                    // program items 처리
                    programItems = (List<Map<String, Object>>)  program.get("items");
                    programNotes = (List<String>) program.get("notes");
                    for(Map<String, Object> item : programItems){
                        if(!CommonUtils.isNullOrBlank(item) && !CommonUtils.isNullOrBlank(item.get("item"))){
                            String name = item.get("item").toString();
                            String strength = item.get("item_status").toString();
                            String tsm = item.get("tsm").toString();
                            programItemEntity = ProgramItem.builder()
                                    .programId(programId)
                                    .itemName(name)
                                    .st(strength)
                                    .tsm(CommonUtils.safeToInt(tsm))
                                    .build();
                            log.info("programItemEntity : {}", programItemEntity);
                            programItemRepository.save(programItemEntity);
                            log.info("programItem saved!");
                        }
                    }
                    if(!CommonUtils.isNullOrBlank(programNotes) && programNotes.size() > 0){
                        for(String note : programNotes){
                            programNoteEntity = ProgramNote.builder()
                                    .programId(programId)
                                    .note(note)
                                    .build();
                            log.info("programNoteEntity : {}", programNoteEntity);
                            programNoteRepository.save(programNoteEntity);
                            log.info("programNote saved!");
                        }
                    }


                }
            }
            /** Programs Data Set End **/
            /** Prompting Hierarchy Data Set Start **/
            String hierarchyId = "";
            PromptingHierarchy promptingHierarchyEntity = null;
            List<Map<String, Object>> activities = null;
            Map<String, Object> promptingRatios = null;
            List<Map<String, Object>> ratios = new ArrayList<>();
            List<Map<String, Object>> ratios2 = new ArrayList<>();
            for(int i = 0; i < promptingHierarchy.size(); i++){
                Map<String, Object> promptingItems = (Map<String, Object>) promptingHierarchy.get(i);
                if(!CommonUtils.isNullOrBlank(promptingItems) && !CommonUtils.isNullOrBlank(promptingItems.get("category"))){
                    hierarchyId = weekCnt + childId + "1" + StringUtils.formatTwoDigits(i+1);
                    promptingRatios = (Map<String, Object>) promptingItems.get("ratios");
                    Map<String, Object> firstRatios = (Map<String, Object>) promptingRatios.get("first_ratio");
                    Map<String, Object> secondRatios = (Map<String, Object>) promptingRatios.get("second_ratio");
                    ratios.add(firstRatios);
                    ratios.add(secondRatios);
                    String tsmObj = secondRatios.get("tsm") != null ? secondRatios.get("tsm").toString() : "0";
                    String sessionCntObj = secondRatios.get("session_cnt") != null ? secondRatios.get("session_cnt").toString() : "0";
                    int tsm = CommonUtils.safeToInt(tsmObj);
                    int sessionCnt = CommonUtils.safeToInt(sessionCntObj);
                    promptingHierarchyEntity = buildPromptingHierarchyEntity(hierarchyId, childId, weekCnt, promptingItems.get("category").toString(),
                            promptingItems.get("sub_category").toString(), tsm, sessionCnt);
                    log.info("promptingHierarchyEntity : {}", promptingHierarchyEntity);
                     promptingHierarchyRepository.save(promptingHierarchyEntity);
                    // prompting_step start
                    activities = (List<Map<String, Object>>) promptingItems.get("activities");
                    savePromptingStepsAndNotes(hierarchyId, "1", activities);
                    savePromptingItems(hierarchyId, childId, ratios);
                }
            }
            /** Prompting Hierarchy Data Set End **/
            /**.prompting Hierarchy2 Data Set Start **/
            for(int i = 0; i < promptingHierarchy2.size(); i++){
                Map<String, Object> promptingItems = (Map<String, Object>) promptingHierarchy2.get(i);
                if(!CommonUtils.isNullOrBlank(promptingItems) && !CommonUtils.isNullOrBlank(promptingItems.get("category"))){
                    hierarchyId = weekCnt + childId + "2" + StringUtils.formatThreeDigits(i+1);
                    promptingRatios = (Map<String, Object>) promptingItems.get("ratios");
                    Map<String, Object> firstRatios = (Map<String, Object>) promptingRatios.get("first_ratio");
                    Map<String, Object> secondRatios = (Map<String, Object>) promptingRatios.get("second_ratio");
                    ratios2.add(firstRatios);
                    ratios2.add(secondRatios);
                    String tsmObj = secondRatios.get("tsm") != null ? secondRatios.get("tsm").toString() : "0";
                    String sessionCntObj = secondRatios.get("session_cnt") != null ? secondRatios.get("session_cnt").toString() : "0";
                    int tsm = CommonUtils.safeToInt(tsmObj);
                    int sessionCnt = CommonUtils.safeToInt(sessionCntObj);
                    String sub_category = promptingItems.get("sub_category") != null ? promptingItems.get("sub_category").toString() : "";
                    promptingHierarchyEntity = buildPromptingHierarchyEntity(hierarchyId, childId, weekCnt, promptingItems.get("category").toString(),
                            sub_category, tsm, sessionCnt);
                    log.info("promptingHierarchyEntity2 : {}", promptingHierarchyEntity);
                    promptingHierarchyRepository.save(promptingHierarchyEntity);
                    activities = (List<Map<String, Object>>) promptingItems.get("activities");
                    savePromptingStepsAndNotes(hierarchyId, "2", activities);
                    savePromptingItems(hierarchyId, childId, ratios);

                }
            }
            log.info("promptingHierarchy End!");
            /**.prompting Hierarchy2 Data Set End **/
            /** reinforcement Samplings Data Set Start **/
            log.info("reinforcement Sampling Start!");
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
            log.info("reinforcement Sampling End!");
            /** reinforcement Samplings Data Set End **/
            /** protests Data Set Start **/
            log.info("Protests Start");
            saveProtests(weekCnt, childId, "short", shortProtests);
            saveProtests(weekCnt, childId, "long", longProtests);
            log.info("Protests End");
            /** protests Data Set End **/
            /** Treatment information Data Set Start **/
            log.info("treatment information Start!");
            saveTreatmentInformation(weekCnt, childId, (List<Map<String, Object>>) treatmentInformation.get("staff"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
                                .build();
                        log.info("promptingStepEntity2 : {}", stepEntity);
                        promptingStepRepository.save(stepEntity).getStepId();
                    }
                    PromptingNote2  noteEntity = null;
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
        for (int j = 0; j < ratios.size(); j++) {
            Map<String, Object> ratio = ratios.get(j);

            if (!CommonUtils.isNullOrBlank(ratio)
                    || !CommonUtils.isNullOrBlank(ratio.get("text"))
                    || !CommonUtils.isNullOrBlank(ratio.get("success_cnt"))
                    || !CommonUtils.isNullOrBlank(ratio.get("attempt_cnt"))
            ) {
                PromptingItem promptingItemEntity = PromptingItem.builder()
                        .hierarchyId(hierarchyId)
                        .childId(childId)
                        .itemName(StringUtils.nvl(
                                CommonUtils.safeToString(ratio.get("text")), "0"))
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

    public void saveProtests(
            String weekCnt,
            String childId,
            String protestsType,
            List<Map<String, Object>> protests

    ){
        String protestsId = "";
        String consequences = "";
        String date = "";
        String function = "";
        String happendedBefore = "";
        String location = "";
        String staff = "";
        String time = "";
        String min = "";
        String sec = "";
        String behaviorName = "";
        String behaviorCnt = "";
        Map<String, Object> duration = null;
        List<Map<String, Object>> behaviors = null;
        Protest protestEntity = null;
        ProtestsDetail protestsDetailEntity = null;
        int idIndex = 0;
        for(int i = 0; i < protests.size(); i++) {
            Map<String, Object> protest = protests.get(i);
            if(!CommonUtils.isNullOrBlank(protest)){
                consequences = protest.get("consequences") != null ? protest.get("consequences").toString() : "";
                date = protest.get("date") != null ? protest.get("date").toString() : "";
                function = protest.get("function") != null ? protest.get("function").toString() : "";
                happendedBefore = protest.get("happended_before") != null ? protest.get("happended_before").toString() : "";
                location = protest.get("location") != null ? protest.get("location").toString() : "";
                staff = protest.get("staff") != null ? protest.get("staff").toString() : "";
                time = protest.get("time") != null ? protest.get("time").toString() : "";
                duration = (Map<String, Object>) protest.get("duration");
                min = duration.get("min") != null ? duration.get("min").toString() : "";
                sec = duration.get("sec") != null ? duration.get("sec").toString() : "";
                ProtestType protestType = null;
                if(protestsType.equals("short")){
                    protestsId = weekCnt + childId + "1" + StringUtils.formatThreeDigits(idIndex + 1);
                    protestType = ProtestType.SHORT;
                }else{
                    protestsId = weekCnt + childId + "2" + StringUtils.formatThreeDigits(idIndex + 1);
                    protestType = ProtestType.LONG;
                }
                if(StringUtils.isNotBlank(staff) && StringUtils.isNotBlank(date) && StringUtils.isNotBlank(time)
                    && StringUtils.isNotBlank(location)
                ) {
                    protestEntity = Protest.builder()
                            .protestId(protestsId)
                            .childId(childId)
                            .weekCnt(weekCnt)
                            .protestType(protestType)
                            .protestDate(date)
                            .protestTime(time)
                            .protestLocation(location)
                            .staffName(staff)
                            .preCondition(happendedBefore)
                            .consequences(consequences)
                            .functionTypeCd(function)
                            .durationMin(min)
                            .durationSec(sec)
                            .build();
                    log.info("protestEntity : {}", protestEntity);
                    protestRepository.save(protestEntity);
                    behaviors = (List<Map<String, Object>>) protest.get("behaviors");
                    for (Map<String, Object> behavior : behaviors) {
                        if (!CommonUtils.isNullOrBlank(behavior)) {
                            behaviorName = behavior.keySet().iterator().next();
                            behaviorCnt = behavior.get(behaviorName) != null ? behavior.get(behaviorName).toString() : "";
                            if (StringUtils.isNotBlank(behaviorCnt)) {
                                protestsDetailEntity = ProtestsDetail.builder()
                                        .protestId(protestsId)
                                        .protestType(protestType)
                                        .behaviorName(behaviorName)
                                        .behaviorCnt(CommonUtils.safeToInt(StringUtils.nvl(behaviorCnt, "0")))
                                        .build();
                                log.info("protestDetailEntity : {}", protestsDetailEntity);
                                protestsDetailRepository.save(protestsDetailEntity);
                            }
                        }
                    }
                    idIndex++;
                }
            }
        }

    }

    // 공통 문자열 캐스팅 함수
    private String safeToString(Object obj) {
        return obj == null ? "" : obj.toString();
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


    public String getWeekCnt(String period){
        String[] weeks = period.split("~");
        String weekCnt = weeks[0].replaceAll("\\.", "");
        return weekCnt;
    }
}
