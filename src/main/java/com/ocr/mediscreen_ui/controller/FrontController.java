package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.model.Patient;
import com.ocr.mediscreen_ui.model.PatientHistory;
import com.ocr.mediscreen_ui.proxies.FrontProxy;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
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
    @RequestMapping("/")
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



    @RequestMapping(value = "Assess", method = RequestMethod.GET)
    String getAssessmentByLastname(@Valid @RequestParam("lastname") String lastname) {
        return frontProxy.getAssessmentByLastname(lastname);
    }

    @GetMapping("/patient/add")
    public String getAddPatientHistory(PatientHistory patientHistory) {
        return "patient/add";
    }

    @PostMapping(value = "/PatHistory/add")
    public String addPatientHistory(@RequestBody PatientHistory patientHistory, Model model, RedirectAttributes redir) {

        try {
            PatientHistory patientAdded = frontProxy.addPatient(patientHistory);
            model.addAttribute("patientAdded", patientAdded);
            redir.addFlashAttribute("success", "Patient successfully added");
        return "patient/add";
    } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + "during operation");
            return "Home";
        }
    }


    @PutMapping(value = "/PatHistory/update/{lastname}")
    PatientHistory updatePatient(@PathVariable String lastname, @RequestBody PatientHistory patientToUpdate) {
        PatientHistory patientHistory = frontProxy.updatePatient(lastname, patientToUpdate);
        return patientHistory;
    }

    @DeleteMapping(value = "/PatHistory/delete/{lastname}")
    PatientHistory deletePatient(@PathVariable String lastname) {
        return frontProxy.deletePatient(lastname);
    }

}
