package com.ocr.mediscreen_ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientHistory {

    private ObjectId _id;

    private Long patId;
    private String lastname;
    private String notes;
}

