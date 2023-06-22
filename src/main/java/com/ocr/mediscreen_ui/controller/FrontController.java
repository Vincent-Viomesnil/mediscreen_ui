package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.model.Patient;
import com.ocr.mediscreen_ui.model.PatientHistory;
import com.ocr.mediscreen_ui.proxies.FrontProxy;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@Slf4j
public class FrontController {

    private final FrontProxy frontProxy;


    public FrontController(FrontProxy frontProxy) {
        this.frontProxy = frontProxy;
    }

    //    @RequestMapping("/")
//    public String home(Model model)
//    {
//        List<PatientHistory> patientHistoryList = frontProxy.patientHistoryList();
//        model.addAttribute("patientHistoryList",patientHistoryList );
//        return "Home";
//    }
    @RequestMapping("/PatientList")
    public String home(Model model) {
        List<Patient> patientList = frontProxy.getPatientList();
        Set<Patient> uniquePatients = new HashSet<>(patientList);
        List<Patient> uniquePatientList = new ArrayList<>(uniquePatients);

        model.addAttribute("uniquePatientList", uniquePatientList);
        return "Home";
    }

    @RequestMapping("/Assess/id/{patId}")
    public String getAssessPatientById(@PathVariable Long patId, Model model) {
        String patientAssessment = frontProxy.getAssessmentById(patId);
        model.addAttribute("patientAssessment", patientAssessment);
        return "Assess";
    }
    @GetMapping(value = "/Patient/id/{id}")
    public String getPatientById(@PathVariable Long id, Model model,RedirectAttributes redir) {
        try {
            Optional<Patient> patient = frontProxy.getPatientById(id);
            model.addAttribute("patient", patient);
            redir.addFlashAttribute("success", "Patient successfully added");
            return "SheetPatient";

        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + "during operation");
            return "Home";
        }
    }


    @GetMapping(value = "/PatHistory/id/{patId}")
    public String getPatientHistoryById(@PathVariable Long patId, Model model,RedirectAttributes redir) {
        try {
        PatientHistory patientNotes = frontProxy.getPatientByPatId(patId);
        model.addAttribute("patientNotes", patientNotes);
        redir.addFlashAttribute("success", "Patient successfully added");
            return "SheetPatient";

        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + "during operation");
            return "Home";
        }
    }
//    @PutMapping(value = "/Patient/update/{id}")
//    public String updatePatient(@PathVariable Long id, @RequestBody Patient patientToUpdate) {
//        Patient patient = frontProxy.updatePatient(id, patientToUpdate);
//        return patient;
//    }

    @DeleteMapping(value = "/Patient/delete/{id}")
    public String deletePatientById(@PathVariable Long id, Model model) {
        Patient patient = new Patient();
        model.addAttribute("patient", patient);
        return "Home";
    }


    @RequestMapping(value = "Assess", method = RequestMethod.GET)
    String getAssessmentByLastname(@Valid @RequestParam("lastname") String lastname) {
        return frontProxy.getAssessmentByLastname(lastname);
    }

//    @GetMapping(value ="/Patient/add")
//    public String getPatient(Patient patient) {
//        return "add";
//    }

    @GetMapping(value ="/Patient/add")
    public String getPatient(Patient patient) {
//        Patient patient = new Patient();
//        model.addAttribute("patient", patient);
//        log.info("The user want to add a new Patient: " +patient);
        return "add";
    }

//    @GetMapping(value ="/Patient/up")
//    public String upPatient(Model model) {
//        Patient patient = new Patient();
//        model.addAttribute("patient", patient);
//        return "patient/update";
//    }

    @PostMapping(value = "/Patient/validate")
    public String addPatient(@ModelAttribute("patient") Patient patient, Model model, RedirectAttributes redir){
        try {
            frontProxy.addPatient(patient);
            List<Patient> patientList = frontProxy.getPatientList();
            Set<Patient> uniquePatients = new HashSet<>(patientList);
            List<Patient> uniquePatientList = new ArrayList<>(uniquePatients);

            model.addAttribute("uniquePatientList", uniquePatientList);

            log.info("The user  added a new Patient: " +patient);
//            log.info("The user  added a new Patient: " +patientAdded);
            return "redirect:/Home";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + "during operation");
            return "add";
        }
    }

    @PostMapping(value = "/PatHistory/add")
    public String addPatientHistory(@RequestBody PatientHistory patientHistory, Model model, RedirectAttributes redir) {

        try {
            PatientHistory patientAdded = frontProxy.addPatientHistory(patientHistory);
            model.addAttribute("patientAdded", patientAdded);
            redir.addFlashAttribute("success", "Patient successfully added");
        return "add";
    } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + "during operation");
            return "Home";
        }
    }


    @PutMapping(value = "/PatHistory/update/{patId}")
    public String updatePatientHistory(@PathVariable Long patId, @RequestBody PatientHistory patientToUpdate, Model model,
    RedirectAttributes redir) {
        try {
            PatientHistory patientHistory = frontProxy.updatePatientById(patId, patientToUpdate);
            model.addAttribute("patientHistory", patientHistory);
            redir.addFlashAttribute("success", "Patient successfully added");
            return "update";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + "during operation");
            return "Home";
        }
    }

    @DeleteMapping(value = "/PatHistory/delete/{id}")
    PatientHistory deletePatient(@PathVariable Long id) {
        return frontProxy.deletePatientById(id);
    }

}
