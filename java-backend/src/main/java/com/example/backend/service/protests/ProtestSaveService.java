package com.example.backend.service.protests;

import com.example.backend.domain.protests.Protest;
import com.example.backend.domain.protests.ProtestType;
import com.example.backend.domain.protests.ProtestsDetail;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.protests.ProtestRepository;
import com.example.backend.repository.protests.ProtestsDetailRepository;
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
public class ProtestSaveService {

    private final ProtestRepository protestRepository;
    private final ProtestsDetailRepository protestsDetailRepository;

    public void save(UploadRequest uploadData, UploadSharedInfo sharedInfo){
        List<Map<String, Object>> longProtests = (List<Map<String, Object>>) uploadData.getProtests_results().get("long_protests");
        List<Map<String, Object>> shortProtests = (List<Map<String, Object>>) uploadData.getProtests_results().get("short_protests");
        String weekCnt = sharedInfo.getWeekCnt();
        String childId = sharedInfo.getChildId();
        saveProtests(weekCnt, childId, "short", shortProtests);
        saveProtests(weekCnt, childId, "long", longProtests);
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
}
