package com.ocr.mediscreen_ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientBean {

    private Long id;

    private String firstname;

    private String lastname;

    private LocalDate birthdate;

    private String gender;

    private String address;
    private String phonenumber;

}

