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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<PatientHistoryBean> patientList = microserviceNotesProxy.patientList();
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
        List<PatientHistoryBean> patientList = microserviceNotesProxy.patientList();

        List<PatientHistoryBean> filteredList = patientList.stream()
                .collect(Collectors.groupingBy(PatientHistoryBean::getPatId))
                .values().stream()
                .flatMap(group -> group.stream().limit(1))
                .collect(Collectors.toList());

        model.addAttribute("filteredList", filteredList);
        return "SheetPatient";
    }

    @GetMapping("/PatHistory/patid/{patId}")
    public String getListNotesByPatId(@PathVariable Long patId, Model model, RedirectAttributes redir) {
        try {
            List<PatientHistoryBean> patientHistory = microserviceNotesProxy.getListNotesByPatId(patId);
            PatientBean patientBean = microservicePatientProxy.getPatientById(patId);
            model.addAttribute("listnotes", patientHistory);
            model.addAttribute("patientBean", patientBean);

            return "Assess";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "HomePH";
        }
    }


    @GetMapping("/assessment/{patId}")
    public String getAssessmentById(@PathVariable Long patId, Model model, RedirectAttributes redir) {
        try {
            List<PatientHistoryBean> patientHistoryBean = microserviceNotesProxy.getListNotesByPatId(patId);
            PatientBean patientBean = microservicePatientProxy.getPatientById(patId);
            String assessment = assessmentProxy.getAssessmentById(patId);

            model.addAttribute("patientBean", patientBean);
            model.addAttribute("listnotes", patientHistoryBean);
            model.addAttribute("assessment", assessment);

            return "AssessmentResult";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "HomePH";
        }
    }

    @GetMapping(value = "/PatHistory/add")
    public String getPatientHistory(Model model) {
        PatientHistoryBean patientHistory = new PatientHistoryBean();

        List<PatientBean> patients = microservicePatientProxy.patientList();
        model.addAttribute("patients", patients);
        model.addAttribute("patientHistory", patientHistory);
        return "AddNote";
    }


    @GetMapping(value="/Patient/add")
    public String getPatient(Model model) {
        PatientBean patient = new PatientBean();
        model.addAttribute("patient", patient);
        return "AddPatient";
    }

    @PostMapping(value = "Patient/validate")
    public String addPatient(PatientBean patient, Model model, RedirectAttributes redir) {
        try {
            PatientBean patientAdded = microservicePatientProxy.addPatient(patient);
            model.addAttribute("patientAdded", patientAdded);
            redir.addFlashAttribute("success", "Patient successfully added");

            List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
            model.addAttribute("uniquePatientList", uniquePatientList);

            return "redirect:/PatientList";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            System.out.println("FeignException");
            return "AddPatient";
        }
    }


    @PostMapping(value = "/PatHistory/validate")
    public String addPatientHistory(PatientHistoryBean patientHistory, Model model, RedirectAttributes redir) {

        try {
            PatientHistoryBean patientAdded = microserviceNotesProxy.addNote(patientHistory);
            model.addAttribute("patientAdded", patientAdded);
            redir.addFlashAttribute("success", "Patient successfully added");

//            PatientBean patient = microservicePatientProxy.getPatientById(p)
            List<PatientHistoryBean> uniquePatientList = microserviceNotesProxy.patientList();
            // où méthode public List<PatientHistoryBean> getUniquePatientHistoryList() {

            model.addAttribute("uniquePatientList", uniquePatientList);
            return "redirect:/";
    } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "AddNote";
        }
    }

    @GetMapping("/Patient/update/{id}")
    public String updatePatientForm(@PathVariable Long id, Model model) {
        PatientBean patient = microservicePatientProxy.getPatientById(id);
        List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();

        model.addAttribute("uniquePatientList", uniquePatientList);
        model.addAttribute("patient", patient);
        return "update";
    }

    @PostMapping(value = "/Patient/update/{id}")
    public String updatePatient(@PathVariable Long id, PatientBean patientToUpdate, Model model,
                                RedirectAttributes redir) {
        try {
            PatientBean patient = microservicePatientProxy.updatePatientById(id, patientToUpdate);
            model.addAttribute("patient", patient);

            List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();

            model.addAttribute("uniquePatientList", uniquePatientList);
            return "redirect:/PatientList";
        } catch (FeignException.BadRequest e) {
            redir.addFlashAttribute("error", "Bad request during operation");
            return "redirect:/PatientList";
        }
    }


    @GetMapping("/PatHistory/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        PatientHistoryBean patientHistory = microserviceNotesProxy.getNoteById(id);
//      PatientHistoryBean patientHistory = new PatientHistoryBean();

        model.addAttribute("patientHistory", patientHistory);

        return "updatePH";
    }
    @PostMapping(value = "/PatHistory/update/{id}")
    public String updatePatientHistory(@PathVariable Long id, PatientHistoryBean patientToUpdate, Model model,
                                       RedirectAttributes redir) {
        try {
            PatientHistoryBean patientHistory = microserviceNotesProxy.updateNoteById(id, patientToUpdate);
            model.addAttribute("patientHistory", patientHistory);
            List<PatientHistoryBean> uniquePatientList = microserviceNotesProxy.patientList();

            model.addAttribute("uniquePatientList", uniquePatientList);
            return "redirect:/";
        } catch (FeignException e) {
            redir.addFlashAttribute("error", e.status() + " during operation");
            return "HomePH";
        }
    }

    @PostMapping(value = "/Patient/delete/{id}")
    public String deletePatient(@PathVariable Long id, Model model) {
        microservicePatientProxy.deletePatientById(id);
        List<PatientBean> uniquePatientList = microservicePatientProxy.patientList();
        model.addAttribute("uniquePatientList", uniquePatientList);
        return "redirect:/PatientList";
    }


    @PostMapping(value = "/PatHistory/delete/{id}")
    public String deletePatientById(@PathVariable Long id, Model model) {
        microserviceNotesProxy.deleteNoteById(id);
        List<PatientHistoryBean> uniquePatientList = microserviceNotesProxy.patientList();
        model.addAttribute("uniquePatientList", uniquePatientList);
        return "redirect:/";
    }

//    public List<PatientHistoryBean> getUniquePatientHistoryList() {
//        List<PatientHistoryBean> patientList = microserviceNotesProxy.patientHistoryList();
//        patientList.stream().collect(Collectors.groupingBy(PatientHistoryBean::getPatId))
//                .values().stream()
//                .filter(group -> group.size() > 1)
//                .map(group -> group.get(0).getPatId())
//                .collect(Collectors.toList());
//        return patientList;
//    }
}
