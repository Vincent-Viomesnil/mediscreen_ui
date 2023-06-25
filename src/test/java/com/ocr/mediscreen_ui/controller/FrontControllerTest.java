//package com.ocr.mediscreen_ui.controller;
//
//import com.ocr.mediscreen_ui.model.PatientBean;
//import com.ocr.mediscreen_ui.model.PatientHistoryBean;
//import com.ocr.mediscreen_ui.proxies.FrontProxy;
//import feign.FeignException;
//import feign.Request;
//import feign.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.ui.Model;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class FrontControllerTest {
//
//    @Mock
//    private FrontProxy frontProxy;
//
//    @Mock
//    private Model model;
//
//    @Mock
//    private RedirectAttributes redirectAttributes;
//
//    @InjectMocks
//    private FrontController frontController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        frontController = new FrontController(frontProxy);
//    }
//
//    @Test
//    void testHomePH() {
//        List<PatientHistoryBean> patientList = frontProxy.patientHistoryList();
//        when(frontProxy.patientHistoryList()).thenReturn(patientList);
//
//        frontController.homePH(model);
//
//        verify(model).addAttribute("patientList", patientList);
//    }
//
//    @Test
//    void testGetPatientHistoryById() {
//        Long patId = 1L;
//        PatientHistoryBean patientHistory = new PatientHistoryBean();
//        when(frontProxy.getPatientByPatId(patId)).thenReturn(patientHistory);
//
//       String result = frontController.getPatientHistoryById(patId, model, redirectAttributes);
//
//
//        assertEquals("Assess", result);
//        verify(frontProxy, times(1)).getPatientByPatId(patId);
//        verify(model, times(1)).addAttribute("patientNotes", patientHistory);
//
//    }
//
//    @Test
//    void testAddPatientHistory() {
//        PatientHistoryBean patientHistory = new PatientHistoryBean();
//        when(frontProxy.addPatientHistory(patientHistory)).thenReturn(patientHistory);
//
//        frontController.addPatientHistory(patientHistory, model, redirectAttributes);
//
//        verify(model).addAttribute("patientAdded", patientHistory);
//        verify(redirectAttributes).addFlashAttribute("success", "Patient successfully added");
//        verify(frontProxy).patientHistoryList();
//
//    }
//
//    @Test
//    void testGetPatientById() {
//        Long id = 1L;
//        Optional<PatientBean> patient = Optional.of(new PatientBean());
//        when(frontProxy.getPatientById(id)).thenReturn(patient);
//
//        String result = frontController.getPatientById(id, model, redirectAttributes);
//
//        assertEquals("SheetPatient", result);
//        verify(frontProxy, times(1)).getPatientById(id);
//        verify(model, times(1)).addAttribute("patient", patient);
//        verify(redirectAttributes, times(1)).addFlashAttribute("success", "Patient successfully added");
//    }
//
//    @Test
//    void testGetPatientByIdFailed() {
//        Long id = 1L;
//        when(frontProxy.getPatientById(id)).thenThrow(FeignException.class);
//
//        String result = frontController.getPatientById(id, model, redirectAttributes);
//
//        assertEquals("Home", result);
//        verify(frontProxy, times(1)).getPatientById(id);
//    }
//
//
//    @Test
//    void testGetAssessPatientById() {
//        Long patId = 1L;
//        String assessment = "Some assessment";
//        when(frontProxy.getAssessmentById(patId)).thenReturn(assessment);
//
//        String result = frontController.getAssessPatientById(patId, model);
//
//        assertEquals("redirect:/Assess/result/" + patId + "?assessment=" + assessment, result);
//        verify(frontProxy, times(1)).getAssessmentById(patId);
//    }
//
//    @Test
//    void testGetAssessmentResult() {
//        Long patId = 1L;
//        String assessment = "Some assessment";
//
//        String result = frontController.getAssessmentResult(patId, assessment, model);
//
//        assertEquals("AssessmentResult", result);
//        verify(model, times(1)).addAttribute("patId", patId);
//        verify(model, times(1)).addAttribute("assessment", assessment);
//    }
//
//    @Test
//    void testUpdateForm() {
//        Long patId = 1L;
//        PatientHistoryBean patientHistory = new PatientHistoryBean();
//        when(frontProxy.getPatientByPatId(patId)).thenReturn(patientHistory);
//
//        String result = frontController.updateForm(patId, model);
//
//        assertEquals("updatePH", result);
//        verify(frontProxy, times(1)).getPatientByPatId(patId);
//        verify(model, times(1)).addAttribute("patientHistory", patientHistory);
//    }
//
//    @Test
//    void testUpdatePatientHistory() {
//
//        Long patId = 1L;
//        PatientHistoryBean patientToUpdate = new PatientHistoryBean();
//        PatientHistoryBean updatedPatientHistory = new PatientHistoryBean();
//        List<PatientHistoryBean> uniquePatientList = new ArrayList<>();
//        when(frontProxy.updatePatientById(patId, patientToUpdate)).thenReturn(updatedPatientHistory);
//        when(frontController.getUniquePatientHistoryList()).thenReturn(uniquePatientList);
//
//        String result = frontController.updatePatientHistory(patId, patientToUpdate, model, redirectAttributes);
//
//        assertEquals("redirect:/", result);
//        verify(frontProxy, times(1)).updatePatientById(patId, patientToUpdate);
//        verify(model, times(1)).addAttribute("patientHistory", updatedPatientHistory);
//        verify(model, times(1)).addAttribute("uniquePatientList", uniquePatientList);
//    }
//
//    @Test
//    void testUpdatePatientHistoryFailed() {
//        Long patId = 1L;
//        PatientHistoryBean patientToUpdate = new PatientHistoryBean();
//        String message = "Invalid request";
//        Request request = Request.create(Request.HttpMethod.GET, "/api/endpoint", Collections.emptyMap(), (byte[]) null, null);
//        Response response = Response.builder()
//                .status(400)
//                .headers(Collections.emptyMap())
//                .reason("Bad Request")
//                .request(request)
//                .build();
//        Throwable cause = null;
//
//        FeignException.BadRequest exception = new FeignException.BadRequest(message, request, response.request().body(),null);
//
//        when(frontProxy.updatePatientById(patId, patientToUpdate)).thenThrow(exception);
//
//        // Act
//        String result = frontController.updatePatientHistory(patId, patientToUpdate, model, redirectAttributes);
//
//        // Assert
//        assertEquals("Home", result);
//        verify(frontProxy, times(1)).updatePatientById(patId, patientToUpdate);
//        verify(redirectAttributes, times(1)).addFlashAttribute("error", "400 during operation");
//    }
//
//    @Test
//    void testDeletePatient() {
//        Long id = 1L;
//        List<PatientHistoryBean> uniquePatientList = new ArrayList<>();
//
//        String result = frontController.deletePatient(id, model);
//
//        assertEquals("redirect:/", result);
//        verify(frontProxy, times(1)).deletePatientById(id);
//        verify(model, times(1)).addAttribute(eq("uniquePatientList"), anyList());
//    }
//
//
//}
