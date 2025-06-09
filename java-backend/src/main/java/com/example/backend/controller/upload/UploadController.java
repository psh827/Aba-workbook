package com.example.backend.controller.upload;

import com.example.backend.dto.upload.UploadRequest;
import com.example.backend.service.upload.UploadService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UploadRequest uploadData){

        log.info("uploadData : {}", uploadData);
        Map<String, Objects> basicInfo = (Map<String, Objects>) uploadData.getBx_info_results().get("basic_info");
        System.out.println(basicInfo.get("name"));

        uploadService.save(uploadData);

        return ResponseEntity.ok(Map.of("message", "success save"));
    }

}
