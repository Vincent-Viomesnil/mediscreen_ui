package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.exceptions.PatientNotFoundException;
import com.ocr.mediscreen_ui.model.PatientBean;
import com.ocr.mediscreen_ui.model.PatientHistoryBean;
import com.ocr.mediscreen_ui.proxies.AssessmentProxy;
import com.ocr.mediscreen_ui.proxies.MicroserviceNotesProxy;
import com.ocr.mediscreen_ui.proxies.MicroservicePatientProxy;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@WebMvcTest(FrontController.class)
public class FrontControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MicroserviceNotesProxy microserviceNotesProxy;

    @MockBean
    private AssessmentProxy assessmentProxy;

    @MockBean
    private MicroservicePatientProxy microservicePatientProxy;

    @Test
    public void testHomePH() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("HomePH"))
                .andExpect(model().attributeExists("patientList"));
    }

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/PatientList"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attributeExists("uniquePatientList"));
    }

    @Test
    public void testGetPatientById() throws Exception {
        long patientId = 1;
        PatientBean patientBean = new PatientBean();
        patientBean.setId(patientId);

        given(microservicePatientProxy.getPatientById(patientId)).willReturn(patientBean);

        mockMvc.perform(get("/Patient/id/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(view().name("Assess"))
                .andExpect(model().attributeExists("patient"))
                .andReturn().getResponse();
    }

    @Test
    public void testGetPatientById_PatientNotFoundException() throws Exception {
        long patientId = 1;

        given(microservicePatientProxy.getPatientById(patientId)).willThrow(PatientNotFoundException.class);

        mockMvc.perform(get("/Patient/id/{id}", patientId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetPatientById_FeignException() throws Exception {
        long patientId = 1;

        given(microservicePatientProxy.getPatientById(patientId)).willThrow(FeignException.class);

        mockMvc.perform(get("/Patient/id/{id}", patientId))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse();
    }

    @Test
    public void testGetPatientHistory() throws Exception {
        List<PatientBean> patients = new ArrayList<>();
        given(microservicePatientProxy.patientList()).willReturn(patients);

        mockMvc.perform(get("/PatHistory/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("AddNote"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", patients))
                .andExpect(model().attributeExists("patientHistory"));
    }

    @Test
    public void testGetPatient() throws Exception {
        mockMvc.perform(get("/Patient/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("AddPatient"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("patient", instanceOf(PatientBean.class)));
    }

    @Test
    public void testAddPatient() throws Exception {
        PatientBean patient = new PatientBean();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setBirthdate(LocalDate.of(1990, 1, 1));

        given(microservicePatientProxy.getPatientById(anyLong())).willReturn(patient);

        mockMvc.perform(post("/Patient/validate")
                        .flashAttr("patient", patient)
                        .sessionAttr("patientExisting", patient))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"))
                .andExpect(flash().attributeExists("success"));
    }

    @Test
    public void testAddPatient_PatientAlreadyExists() throws Exception {
        PatientBean patient = new PatientBean();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setBirthdate(LocalDate.of(1990, 1, 1));

        PatientBean patientExisting = new PatientBean();
        patientExisting.setFirstname("John");
        patientExisting.setLastname("Doe");
        patientExisting.setBirthdate(LocalDate.of(1990, 1, 1));

        given(microservicePatientProxy.getPatientById(anyLong())).willReturn(patientExisting);

        mockMvc.perform(post("/Patient/validate")
                        .flashAttr("patient", patient)
                        .sessionAttr("patientExisting", patientExisting))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"));
    }

    @Test
    public void testAddPatientHistory() throws Exception {
        PatientHistoryBean patientHistory = new PatientHistoryBean();

        given(microserviceNotesProxy.addNote(patientHistory)).willReturn(patientHistory);

        mockMvc.perform(post("/PatHistory/validate")
                        .flashAttr("patientHistory", patientHistory))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testUpdatePatientForm() throws Exception {
        long patientId = 1;
        PatientBean patient = new PatientBean();
        List<PatientBean> uniquePatientList = new ArrayList<>();

        given(microservicePatientProxy.getPatientById(patientId)).willReturn(patient);
        given(microservicePatientProxy.patientList()).willReturn(uniquePatientList);

        mockMvc.perform(get("/Patient/update/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(view().name("Update"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attributeExists("uniquePatientList"))
                .andExpect(model().attribute("uniquePatientList", uniquePatientList));
    }

    @Test
    public void testUpdatePatient_Success() throws Exception {
        long patientId = 1;
        PatientBean patientToUpdate = new PatientBean();
        PatientBean patientUpdated = new PatientBean();
        List<PatientBean> uniquePatientList = new ArrayList<>();

        given(microservicePatientProxy.updatePatientById(patientId, patientToUpdate)).willReturn(patientUpdated);
        given(microservicePatientProxy.patientList()).willReturn(uniquePatientList);

        mockMvc.perform(post("/Patient/update/{id}", patientId)
                        .flashAttr("patientToUpdate", patientToUpdate))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"));
    }

    @Test
    public void testUpdatePatient_BadRequest() throws Exception {
        long patientId = 1;
        PatientBean patientToUpdate = new PatientBean();

        given(microservicePatientProxy.updatePatientById(patientId, patientToUpdate)).willThrow(FeignException.BadRequest.class);

        mockMvc.perform(post("/Patient/update/{id}", patientId)
                        .flashAttr("patientToUpdate", patientToUpdate))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"));
    }

    @Test
    public void testUpdateForm() throws Exception {
        long noteId = 1;
        PatientHistoryBean patientHistory = new PatientHistoryBean();

        given(microserviceNotesProxy.getNoteById(noteId)).willReturn(patientHistory);

        mockMvc.perform(get("/PatHistory/update/{id}", noteId))
                .andExpect(status().isOk())
                .andExpect(view().name("UpdatePH"))
                .andExpect(model().attributeExists("patientHistory"))
                .andExpect(model().attribute("patientHistory", patientHistory));
    }

    @Test
    public void testUpdatePatientHistory_Success() throws Exception {
        long noteId = 1;
        PatientHistoryBean patientToUpdate = new PatientHistoryBean();
        PatientHistoryBean patientHistory = new PatientHistoryBean();
        List<PatientHistoryBean> uniquePatientList = new ArrayList<>();

        given(microserviceNotesProxy.updateNoteById(noteId, patientToUpdate)).willReturn(patientHistory);
        given(microserviceNotesProxy.patientList()).willReturn(uniquePatientList);

        mockMvc.perform(post("/PatHistory/update/{id}", noteId)
                        .flashAttr("patientToUpdate", patientToUpdate))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testUpdatePatientHistory_FeignException() throws Exception {
        long noteId = 1;
        PatientHistoryBean patientToUpdate = new PatientHistoryBean();

        given(microserviceNotesProxy.updateNoteById(noteId, patientToUpdate)).willThrow(FeignException.InternalServerError.class);

        mockMvc.perform(post("/PatHistory/update/{id}", noteId)
                        .flashAttr("patientToUpdate", patientToUpdate))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testDeletePatient() throws Exception {
        long patientId = 1;
        List<PatientBean> uniquePatientList = new ArrayList<>();

        mockMvc.perform(post("/Patient/delete/{id}", patientId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/PatientList"));
    }

    @Test
    public void testDeletePatientHistory_Success() throws Exception {
        long noteId = 1;
        List<PatientHistoryBean> uniquePatientList = new ArrayList<>();

        mockMvc.perform(post("/PatHistory/delete/{id}", noteId))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testPatientList() throws Exception {
        List<PatientBean> patients = new ArrayList<>();

        given(microservicePatientProxy.patientList()).willReturn(patients);

        mockMvc.perform(get("/PatientList"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"));
    }


    @Test
    public void testGetPatientDetails() throws Exception {
        long patientId = 1;
        String assessmentResult = "Some assessment result";
        List<PatientHistoryBean> patientHistoryList = new ArrayList<>();

        given(microserviceNotesProxy.getListNotesByPatId(patientId)).willReturn(patientHistoryList);

        given(assessmentProxy.getAssessmentById(patientId)).willReturn(assessmentResult);

        mockMvc.perform(get("/PatHistory/patid/{patId}", patientId))
                .andExpect(status().isOk())
                .andExpect(view().name("PatientDetails"))
                .andExpect(model().attributeExists("patientHistoryList"))
                .andExpect(model().attribute("patientHistoryList", patientHistoryList))
                .andExpect(model().attributeExists("assessmentResult"))
                .andExpect(model().attribute("assessmentResult", assessmentResult));

    }

    @Test
    public void testGetSheetPatient_Success() throws Exception {
        List<PatientHistoryBean> patientList = new ArrayList<>();
        List<PatientHistoryBean> filteredList = new ArrayList<>();

        given(microserviceNotesProxy.patientList()).willReturn(patientList);

        mockMvc.perform(get("/PatientHistoryList/Filter"))
                .andExpect(status().isOk())
                .andExpect(view().name("Assess"))
                .andExpect(model().attributeExists("filteredList"))
                .andExpect(model().attribute("filteredList", filteredList));

    }
}




