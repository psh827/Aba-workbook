package com.example.backend.service.programs;

import com.example.backend.domain.programs.Program;
import com.example.backend.domain.programs.ProgramItem;
import com.example.backend.domain.programs.ProgramNote;
import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.dto.upload.UploadSharedInfo;
import com.example.backend.repository.programs.ProgramItemRepository;
import com.example.backend.repository.programs.ProgramNoteRepository;
import com.example.backend.repository.programs.ProgramRepository;
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
public class ProgramSaveService {

    private final ProgramRepository programRepository;
    private final ProgramItemRepository programItemRepository;
    private final ProgramNoteRepository programNoteRepository;

    public void save(UploadRequest uploadData, UploadSharedInfo sharedInfo) {
        Map<String, Object> programsMap = (Map<String, Object>) uploadData.getPrograms_result().get("programs");

        log.info("programs Data start!");
        for (int i = 0; i < programsMap.size(); i++) {
            Map<String, Object> program = (Map<String, Object>) programsMap.get("program" + (i + 1));

            if (CommonUtils.isNullOrBlank(program) || CommonUtils.isNullOrBlank(program.get("program_name"))) {
                continue;
            }

            String programId = sharedInfo.getWeekCnt() + sharedInfo.getChildId() + "1" + StringUtils.formatThreeDigits(i + 1);

            // ✅ Program 저장
            Program programEntity = Program.builder()
                    .programId(programId)
                    .childId(sharedInfo.getChildId())
                    .weekCnt(sharedInfo.getWeekCnt())
                    .programName(program.get("program_name").toString())
                    .sessionCnt(CommonUtils.safeToInt(program.get("session_cnt")))
                    .build();
            programRepository.save(programEntity);
            log.info("✅ program saved: {}", programEntity);

            // ✅ ProgramItems 저장
            List<Map<String, Object>> items = (List<Map<String, Object>>) program.get("items");
            if (items != null) {
                for (Map<String, Object> item : items) {
                    if (!CommonUtils.isNullOrBlank(item.get("item"))) {
                        ProgramItem itemEntity = ProgramItem.builder()
                                .programId(programId)
                                .itemName(item.get("item").toString())
                                .st(item.get("item_status").toString())
                                .tsm(CommonUtils.safeToInt(item.get("tsm")))
                                .build();
                        programItemRepository.save(itemEntity);
                        log.info("✅ programItem saved: {}", itemEntity);
                    }
                }
            }

            // ✅ ProgramNotes 저장
            List<String> notes = (List<String>) program.get("notes");
            if (notes != null && !notes.isEmpty()) {
                for (String note : notes) {
                    ProgramNote noteEntity = ProgramNote.builder()
                            .programId(programId)
                            .note(note)
                            .build();
                    programNoteRepository.save(noteEntity);
                    log.info("✅ programNote saved: {}", noteEntity);
                }
            }
        }
    }
}
