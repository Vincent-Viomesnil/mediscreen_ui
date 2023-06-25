package com.ocr.mediscreen_ui.proxies;

import com.ocr.mediscreen_ui.model.PatientBean;
import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "mediscreen-assess", url = "${mediscreen-assess.url}")
public interface AssessmentProxy {

    @GetMapping(value = "assessment/id/{patId}")
    String getAssessmentById(@Valid @PathVariable Long patId);
}