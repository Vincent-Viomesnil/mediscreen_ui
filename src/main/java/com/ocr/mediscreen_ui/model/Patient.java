package com.ocr.mediscreen_ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private Long id;

    private String firstname;

    private String lastname;

    private Date birthdate;

    private String gender;

    private String address;
    private String phonenumber;

}

