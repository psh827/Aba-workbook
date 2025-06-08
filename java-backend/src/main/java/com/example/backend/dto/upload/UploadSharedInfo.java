package com.example.backend.dto.upload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadSharedInfo {
    private final String weekCnt;
    private final String childId;
    private final String childName;
    private final String leaderName;
}
