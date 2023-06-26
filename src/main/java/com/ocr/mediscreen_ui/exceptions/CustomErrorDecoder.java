package com.ocr.mediscreen_ui.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String income, Response response) {
        if(response.status() == 400 ) {
            return new PatientNotFoundException(
                    "Request Incorrect "
            );
        }
        return defaultErrorDecoder.decode(income, response);
    }
}