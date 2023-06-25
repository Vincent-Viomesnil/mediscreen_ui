package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.model.PatientBean;
import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import com.ocr.mediscreen_ui.proxies.AssessmentProxy;
import com.ocr.mediscreen_ui.proxies.MicroserviceNotesProxy;
import com.ocr.mediscreen_ui.proxies.MicroservicePatientProxy;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class FrontController {

    @Autowired
    private MicroserviceNotesProxy microserviceNotesProxy;
    @Autowired
    private AssessmentProxy assessmentProxy;
    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;


    @GetMapping("/")
    public String homePH(Model model) {
        List<PatientHistoryBean> patientList = getUniquePatientHistoryList();
        model.addAttribute("patientList", patientList);
        return "HomePH";
    }

    @GetMapping("/PatientList")
    public String home(Model model) {
        List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();

        model.addAttribute("uniquePatientList", uniquePatientList);
        return "Home";
    }
    @GetMapping(value = "/Patient/id/{id}")
    public String getPatientById(@PathVariable Long id, Model model,RedirectAttributes redir) {
        try {
            PatientBean patient = microservicePatientProxy.getPatientById(id);
            model.addAttribute("patient", patient);
            redir.addFlashAttribute("success", "Patient successfully added");
            return "SheetPatient";

        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "Home";
        }
    }

    @GetMapping("/PatientHistoryList/Filter")
    public String getSheetPatient(Model model) {
        List<PatientHistoryBean> patientList = microserviceNotesProxy.patientHistoryList();

        List<PatientHistoryBean> filteredList = patientList.stream()
                .collect(Collectors.groupingBy(PatientHistoryBean::getPatId))
                .values().stream()
                .flatMap(group -> group.stream().limit(1))
                .collect(Collectors.toList());

        model.addAttribute("filteredList", filteredList);
        return "SheetPatient";
    }



    @GetMapping("/PatHistory/note/{patId}")
    public String getPatientHistoryById(@PathVariable Long patId, Model model, RedirectAttributes redir) {
        try {
            PatientHistoryBean patientHistory = microserviceNotesProxy.getPatientByPatId(patId);
            model.addAttribute("patientHistory", patientHistory);
            return "Assess";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "HomePH";
        }
    }


    @GetMapping("/assessment/id/{patId}")
    public String getAssessmentById(@PathVariable Long patId, Model model, RedirectAttributes redir) {
        try {
            // Récupérer les informations nécessaires pour l'assessment
            PatientHistoryBean patientHistory = microserviceNotesProxy.getPatientByPatId(patId);
            String assessment = assessmentProxy.getAssessmentById(patId);

            // Ajouter les données à l'objet Model
            model.addAttribute("patId", patId);
            model.addAttribute("lastname", patientHistory.getLastname());
            model.addAttribute("notes", patientHistory.getNotes());
            model.addAttribute("assessment", assessment);

            return "AssessmentResult";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "AddPatient";
        }
    }

    @GetMapping(value ="/PatHistory/add")
    public String getPatientHistory(Model model) {
        PatientHistoryBean patientHistory = new PatientHistoryBean();
        model.addAttribute("patientHistory", patientHistory);
        log.info("The user want to add a new Patient: " +patientHistory);
        return "addPH";
    }

    @GetMapping(value="/Patient/add")
    public String getPatient(Model model) {
        PatientBean patient = new PatientBean();
        model.addAttribute("patient", patient);
        return "AddPatient";
    }


    @PostMapping(value = "/Patient/add")
    public String addPatient(PatientBean patient, Model model, RedirectAttributes redir) {
        try {
            PatientBean patientAdded = microservicePatientProxy.addPatient(patient);
            model.addAttribute("patientAdded", patientAdded);
            redir.addFlashAttribute("success", "Patient successfully added");

            List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
            model.addAttribute("uniquePatientList", uniquePatientList);

            System.out.println("Patientadded");

            return "redirect:/PatientList";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            System.out.println("FeignException");
            return "AddPatient";
        }
    }


    @PostMapping(value = "/PatHistory/add")
    public String addPatientHistory(PatientHistoryBean patientHistory, Model model, RedirectAttributes redir) {

        try {
            PatientHistoryBean patientAdded = microserviceNotesProxy.addPatientHistory(patientHistory);
            model.addAttribute("patientAdded", patientAdded);
            redir.addFlashAttribute("success", "Patient successfully added");

            List<PatientHistoryBean> uniquePatientList = getUniquePatientHistoryList();


            model.addAttribute("uniquePatientList", uniquePatientList);
            return "redirect:/";
    } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "addPH";
        }
    }

    @GetMapping("/PatHistory/update/{patId}")
    public String updateForm(@PathVariable Long patId, Model model) {
        PatientHistoryBean patientHistory = microserviceNotesProxy.getPatientByPatId(patId);
        model.addAttribute("patientHistory", patientHistory);
        return "updatePH";
    }

    @GetMapping("/Patient/update/{id}")
    public String updatePatientForm(@PathVariable Long id, Model model) {
        PatientBean patient = microservicePatientProxy.getPatientById(id);
        model.addAttribute("patient", patient);
        return "update";
    }

    @PostMapping(value = "/Patient/update/{id}")
    public String updatePatient(@PathVariable Long id, PatientBean patientToUpdate, Model model,
                                RedirectAttributes redir) {
        try {
            PatientBean patient = microservicePatientProxy.updatePatient(id, patientToUpdate);
            model.addAttribute("patient", patient);

            List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();

            model.addAttribute("uniquePatientList", uniquePatientList);
            return "redirect:/PatientList";
        } catch (FeignException.BadRequest e) {
            redir.addFlashAttribute("error", "Bad request during operation");
            return "redirect:/PatientList";
        }
    }
    @PostMapping(value = "/PatHistory/update/{patId}")
    public String updatePatientHistory(@PathVariable Long patId, PatientHistoryBean patientToUpdate, Model model,
                                       RedirectAttributes redir) {
        try {
            PatientHistoryBean patientHistory = microserviceNotesProxy.updatePatientById(patId, patientToUpdate);
            model.addAttribute("patientHistory", patientHistory);
            List<PatientHistoryBean> uniquePatientList = getUniquePatientHistoryList();

            model.addAttribute("uniquePatientList", uniquePatientList);
            return "redirect:/";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "HomePH";
        }
    }

    @PostMapping(value = "/Patient/delete/{id}")
    public String deletePatient(@PathVariable Long id, Model model) {
        microservicePatientProxy.deletePatient(id);
        List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
        model.addAttribute("uniquePatientList", uniquePatientList);
        return "redirect:/PatientList";
    }


    @PostMapping(value = "/PatHistory/delete/{id}")
    public String deletePatientById(@PathVariable Long id, Model model) {
        microserviceNotesProxy.deletePatientById(id);
        List<PatientHistoryBean> uniquePatientList = getUniquePatientHistoryList();
        model.addAttribute("uniquePatientList", uniquePatientList);
        return "redirect:/";
    }

    public List<PatientHistoryBean> getUniquePatientHistoryList() {
        List<PatientHistoryBean> patientList = microserviceNotesProxy.patientHistoryList();
        patientList.stream().collect(Collectors.groupingBy(PatientHistoryBean::getPatId))
                .values().stream()
                .filter(group -> group.size() > 1)
                .map(group -> group.get(0).getPatId())
                .collect(Collectors.toList());
        return patientList;
    }
}
