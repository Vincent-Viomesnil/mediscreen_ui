package com.ocr.mediscreen_ui.proxies;

import com.ocr.mediscreen_ui.model.Patient;
import com.ocr.mediscreen_ui.model.PatientHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@FeignClient(name = "mediscreen-assess", url = "localhost:8080")
public interface FrontProxy {
    @RequestMapping(value="/PatHistoryList", method = RequestMethod.GET)
    List<PatientHistory> patientHistoryList();

    @RequestMapping(value = "Assess", method = RequestMethod.GET)
    String getAssessmentByLastname(@Valid @RequestParam("lastname") String lastname);

    @GetMapping(value = "Assess/id/{patId}")
    String getAssessmentById(@Valid @PathVariable Long patId);

    @PostMapping(value = "/PatHistory/add")
    PatientHistory addPatient(@Valid @RequestBody PatientHistory patientHistory);

    @PutMapping(value = "/PatHistory/update/{lastname}")
    PatientHistory updatePatient(@PathVariable String lastname, @RequestBody PatientHistory patientToUpdate);

    @DeleteMapping(value= "/PatHistory/delete/{lastname}")
    PatientHistory deletePatient(@PathVariable String lastname);
    @GetMapping(value = "/PatHistory/id/{patId}")
    PatientHistory getPatientByPatId(@PathVariable Long patId);
    @RequestMapping(value="/Patients", method = RequestMethod.GET)
    List<Patient> getPatientList();

    @GetMapping(value = "Patient/{lastname}")
    Optional<Patient> getPatientByLastname(@Valid @PathVariable("lastname") String lastname);

    @PostMapping(value = "/Patient/add")
    Patient addPatient(@RequestBody Patient patient);

    @PutMapping(value = "/Patient/update/{lastname}")
    Patient updatePatient(@PathVariable String lastname, @RequestBody Patient patientToUpdate);

//    @DeleteMapping(value= "/Patient/delete/{lastname}")
//    Patient deletePatient(@PathVariable String lastname);

}
