package com.example.backend.dto.upload;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UploadRequest {
    private Map<String, Object> bx_info_results;
    private Map<String, Object> home_protests;
    private Map<String, Object> tracking_data;
    private Map<String, Object> treatment_information;
    private Map<String, Object> programs_result;
    private Map<String, Object> protests_results;
    private Map<String, Object> maintenance;
    private String folder;
    private String message;
}

