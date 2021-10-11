package ru.max.external.application.messaging.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegistrationResponse {

    String state;

    String errorDescription;
}
