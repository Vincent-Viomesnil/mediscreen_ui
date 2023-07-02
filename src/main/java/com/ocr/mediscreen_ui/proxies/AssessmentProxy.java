package com.ocr.mediscreen_ui.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mediscreen-assess", url = "${mediscreen-assess.url}")
public interface AssessmentProxy {

    @GetMapping(value = "/assessment/{patId}")
    String getAssessmentById(@PathVariable Long patId);

}