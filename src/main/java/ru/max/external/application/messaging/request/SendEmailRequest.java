package ru.max.external.application.messaging.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {

    String recipient;

    String subject;

    String body;
}
