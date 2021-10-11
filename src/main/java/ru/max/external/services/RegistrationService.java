package ru.max.external.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.max.external.application.messaging.request.RegistrationRequest;
import ru.max.external.application.messaging.response.RegistrationResponse;

@Log4j2
@Component
@RequiredArgsConstructor
public class RegistrationService {

    RegistrationResponse registration (RegistrationRequest message) {

        RegistrationResponse response;

        //Логика в рамках тестового задания
        if (!message.getLogin().matches(".*\\d+.*")) {

            response = RegistrationResponse.builder()
                    .state("Ok")
                    .build();
        } else {

            response = RegistrationResponse.builder()
                    .state("Error")
                    .errorDescription("Login has digit symbol")
                    .build();
        }

        return response;
    }
}
