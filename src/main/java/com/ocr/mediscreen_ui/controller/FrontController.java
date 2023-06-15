package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.model.Patient;
import com.ocr.mediscreen_ui.model.PatientHistory;
import com.ocr.mediscreen_ui.proxies.FrontProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
public String home(Model model)
{
    List<Patient> patientList = frontProxy.getPatientList();

    model.addAttribute("patientList",patientList );
    return "Home";
}

    @RequestMapping("/Assess/id/{patId}")
    public String getAssessPatientById(@PathVariable Long patId,  Model model){
        String patientHistory = frontProxy.getAssessmentById(patId);
        model.addAttribute("patientHistory", patientHistory);
        return "SheetPatient";
    }


//    @GetMapping(value = "Assess/id/{patId}")
//    String getAssessmentById(@Valid @PathVariable Long patId) {
//        return frontProxy.getAssessmentById(patId);
//    }
    @GetMapping(value = "/PatHistory/id/{patId}")
    String getPatientHistoryById(@PathVariable Long patId, Model model) {
        PatientHistory notes = frontProxy.getPatientByPatId(patId);
        model.addAttribute("notes", notes);
        return "Notes";
    }

//    @RequestMapping(value="/PatHistoryList", method = RequestMethod.GET)
//    public List<PatientHistory> patientHistoryList() {
//        return frontProxy.patientHistoryList();
//    }


    @RequestMapping(value = "Assess", method = RequestMethod.GET)
    String getAssessmentByLastname(@Valid @RequestParam("lastname") String lastname) {
        return frontProxy.getAssessmentByLastname(lastname);
    }



    @PostMapping(value = "/PatHistory/add")
    public ResponseEntity<Object> addPatient(@RequestBody PatientHistory patientHistory) {
        ResponseEntity<Object> patientAdded = frontProxy.addPatient(patientHistory);
        return patientAdded;
    }

    @PutMapping(value = "/PatHistory/update/{lastname}")
    PatientHistory updatePatient(@PathVariable String lastname, @RequestBody PatientHistory patientToUpdate) {
        PatientHistory patientHistory = frontProxy.updatePatient(lastname, patientToUpdate);
        return patientHistory;
    }
//
//    @DeleteMapping(value = "/PatHistory/delete/{lastname}")
//    PatientHistory deletePatient(@PathVariable String lastname) {
//        PatientHistory patientHistory = frontProxy.deletePatient(lastname);
//        return patientHistory;
//    }

}
