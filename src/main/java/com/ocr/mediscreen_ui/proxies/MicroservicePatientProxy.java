package com.ocr.mediscreen_ui.proxies;

import com.ocr.mediscreen_ui.model.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "mediscreen", url = "${mediscreen.url}")
public interface MicroservicePatientProxy {


    @GetMapping(value = "/Patients")
    List<PatientBean> patientList();

    @GetMapping(value = "/Patient/id/{id}")
    PatientBean getPatientById(@PathVariable Long id);

    @PostMapping(value = "/Patient/add")
    PatientBean addPatient(PatientBean patient);

    @PutMapping(value = "/Patient/update/{id}")
    PatientBean updatePatientById(@PathVariable Long id, PatientBean patientToUpdate);

    @DeleteMapping(value = "/Patient/delete/{id}")
    void deletePatientById(@PathVariable Long id);

}