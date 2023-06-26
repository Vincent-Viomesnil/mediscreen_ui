package com.ocr.mediscreen_ui.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientHistoryBean {

    private ObjectId _id;
    private Long noteId;
    private Long patId;
    private String lastname;
    private String notes;
}

