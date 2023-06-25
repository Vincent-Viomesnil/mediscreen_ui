package com.ocr.mediscreen_ui.proxies;

import com.ocr.mediscreen_ui.model.PatientBean;
import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@FeignClient(name = "mediscreen-assess", url = "localhost:8080")
public interface FrontProxy {

    @GetMapping(value = "/PatHistoryList")
    List<PatientHistoryBean> patientHistoryList();

    @GetMapping(value = "/PatHistory/lastname/{lastname}")
    PatientHistoryBean getPatientHistoryByLastname(@PathVariable("lastname") String lastname);

    @GetMapping(value = "/PatHistory/id/{patId}")
    PatientHistoryBean getPatientByPatId(@PathVariable Long patId);
    @GetMapping(value = "/Patients")
    List<PatientBean> patientList();

    @GetMapping(value = "/Patient/lastname/{lastname}")
    PatientBean getPatientByLastname(@PathVariable("lastname") String lastname);

    @GetMapping(value = "Patient/id/{id}")
    PatientBean getPatientById(@PathVariable Long id);


    @RequestMapping(value = "Assess", method = RequestMethod.GET)
    String getAssessmentByLastname(@RequestParam("lastname") String lastname);

    @GetMapping(value = "Assess/id/{patId}")
    String getAssessmentById(@Valid @PathVariable Long patId);


    @PostMapping(value = "/PatHistory/add")
    PatientHistoryBean addPatientHistory(@RequestBody PatientHistoryBean patientHistory);

    @PostMapping(value = "/Patient/add")
    PatientBean addPatient(@RequestBody PatientBean patient);

    @PutMapping(value = "/PatHistory/update/{patId}")
    PatientHistoryBean updatePatientById(@PathVariable Long patId, @RequestBody PatientHistoryBean patientToUpdate);

    @DeleteMapping(value = "/PatHistory/delete/{patId}")
    PatientHistoryBean deletePatientById(@PathVariable Long patId);

    @PutMapping(value = "/Patient/update/{id}")
    PatientBean updatePatient(@PathVariable Long id, PatientBean patientToUpdate);

    @DeleteMapping(value = "/Patient/delete/{id}")
    PatientBean deletePatient(@PathVariable Long id);

}