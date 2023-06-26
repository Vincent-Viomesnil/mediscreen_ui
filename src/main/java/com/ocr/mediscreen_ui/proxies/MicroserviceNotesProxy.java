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
    List<PatientHistoryBean> patientList();

    @GetMapping(value = "/PatHistory/patid/{patId}")
     List<PatientHistoryBean> getListNotesByPatId(@PathVariable Long patId);

    @GetMapping(value = "/PatHistory/noteid/{noteId}")
    PatientHistoryBean getNoteById(@PathVariable Long noteId);

    @PostMapping(value = "/PatHistory/add")
    PatientHistoryBean addNote(@RequestBody PatientHistoryBean patientHistory);

    @PutMapping("/PatHistory/update/{id}")
    PatientHistoryBean updateNoteById(@PathVariable Long id, @RequestBody PatientHistoryBean patientNoteToUpdate);

    @DeleteMapping(value= "/PatHistory/delete/{noteId}")
    void deleteNoteById(@PathVariable Long noteId);


}