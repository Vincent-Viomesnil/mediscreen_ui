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
    @RequestMapping(value = "/PatHistoryList", method = RequestMethod.GET)
    List<PatientHistory> patientHistoryList();

    @RequestMapping(value = "/Patients", method = RequestMethod.GET)
    List<Patient> getPatientList();

    @GetMapping(value = "Patient/{lastname}")
    Optional<Patient> getPatientByLastname(@Valid @PathVariable("lastname") String lastname);

    @GetMapping(value = "Patient/id/{id}")
    Optional<Patient> getPatientById(@PathVariable Long id);

    @RequestMapping(value = "Assess", method = RequestMethod.GET)
    String getAssessmentByLastname(@Valid @RequestParam("lastname") String lastname);

    @GetMapping(value = "Assess/id/{patId}")
    String getAssessmentById(@Valid @PathVariable Long patId);

    @GetMapping(value = "/PatHistory/id/{patId}")
    PatientHistory getPatientByPatId(@PathVariable Long patId);

    @GetMapping(value = "/PatHistory/lastname/{lastname}")
    PatientHistory getPatientByPatId(@PathVariable String lastname);

    @PostMapping(value = "/PatHistory/add")
    PatientHistory addPatientHistory(@Valid @RequestBody PatientHistory patientHistory);

    @PostMapping(value = "/Patient/add")
    Patient addPatient(@RequestBody Patient patient);

//    @RequestMapping(value = "PatHistory/update", method = RequestMethod.PUT)
//    PatientHistory updatePatientByLastname(@RequestParam("lastname") String lastname,
//                                           @RequestBody PatientHistory patientToUpdate);

    @PutMapping(value = "/PatHistory/update/{patId}")
    PatientHistory updatePatientById(@PathVariable Long patId, @RequestBody PatientHistory patientToUpdate);

//    @RequestMapping(value = "PatHistory/delete", method = RequestMethod.DELETE)
//    PatientHistory deletePatientByLastname(@RequestParam("lastname") String lastname);

    @DeleteMapping(value = "/PatHistory/delete/{patId}")
    PatientHistory deletePatientById(@PathVariable Long patId);

//    @RequestMapping(value = "Patient/update", method = RequestMethod.PUT)
//    Patient updatePatientByLastname(@RequestParam("lastname") String lastname, @RequestBody Patient patientToUpdate);
//
//    @RequestMapping(value = "Patient/delete", method = RequestMethod.DELETE)
//    Patient deletePatientByLastname(@RequestParam("lastname") String lastname);

    @PutMapping(value = "/Patient/update/{id}")
    Patient updatePatient(@PathVariable Long id,Patient patientToUpdate);

    @DeleteMapping(value = "/Patient/delete/{id}")
    Patient deletePatient(@PathVariable Long id);

}