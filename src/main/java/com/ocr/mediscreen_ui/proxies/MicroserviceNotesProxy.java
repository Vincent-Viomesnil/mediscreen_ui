package com.ocr.mediscreen_ui.proxies;

import com.ocr.mediscreen_ui.model.PatientBean;
import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "mediscreen-mdb", url = "${mediscreen-mdb.url}")
public interface MicroserviceNotesProxy {

    @GetMapping(value = "/PatHistoryList")
    List<PatientHistoryBean> patientHistoryList();

    @GetMapping(value = "/PatHistory/id/{patId}")
    PatientHistoryBean getPatientByPatId(@PathVariable Long patId);

    @PostMapping(value = "/PatHistory/add")
    PatientHistoryBean addPatientHistory(@RequestBody PatientHistoryBean patientHistory);

    @PutMapping(value = "/PatHistory/update/{patId}")
    PatientHistoryBean updatePatientById(@PathVariable Long patId, @RequestBody PatientHistoryBean patientToUpdate);

    @DeleteMapping(value = "/PatHistory/delete/{patId}")
    PatientHistoryBean deletePatientById(@PathVariable Long patId);


}