package com.ocr.mediscreen_ui.controller;

import com.ocr.mediscreen_ui.model.PatientHistory;
import com.ocr.mediscreen_ui.proxies.FrontProxy;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class FrontControllerTest {

    @Mock
    private FrontProxy frontProxy;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private FrontController frontController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHomePH() {
        List<PatientHistory> patientList = new ArrayList<>();
        when(frontProxy.patientHistoryList()).thenReturn(patientList);

        frontController.homePH(model);

        verify(model).addAttribute("uniquePatientList", patientList);
    }

    @Test
    void testGetPatientHistoryById() {
        Long patId = 1L;
        PatientHistory patientHistory = new PatientHistory();
        when(frontProxy.getPatientByPatId(patId)).thenReturn(patientHistory);

        frontController.getPatientHistoryById(patId, model, redirectAttributes);

        verify(model).addAttribute("patientNotes", patientHistory);
        verify(redirectAttributes).addFlashAttribute("success", "Patient successfully added");
    }

    @Test
    void testAddPatientHistory() {
        PatientHistory patientHistory = new PatientHistory();
        when(frontProxy.addPatientHistory(patientHistory)).thenReturn(patientHistory);

        frontController.addPatientHistory(patientHistory, model, redirectAttributes);

        verify(model).addAttribute("patientAdded", patientHistory);
        verify(redirectAttributes).addFlashAttribute("success", "Patient successfully added");
        verify(frontProxy).patientHistoryList();
//        verify(model).addAttribute("uniquePatientList", anyList());
    }


}

