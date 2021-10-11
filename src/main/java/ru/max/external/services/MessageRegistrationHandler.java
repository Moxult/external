package ru.max.external.services;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.max.external.application.messaging.request.RegistrationRequest;
import ru.max.external.application.messaging.response.RegistrationResponse;
import ru.max.external.application.messaging.request.SendEmailRequest;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageRegistrationHandler {

    @NonNull RegistrationService registrationService;
    @NonNull EmailService emailSender;

    @RabbitListener(queues = {"#{queue}"})
    @SneakyThrows
    public RegistrationResponse receiveRequestAndResponse(RegistrationRequest message) {

        return registrationService.registration(message);
    }


    @RabbitListener(queues = {"#{queueEvent}"})
    public void receiveRequestEvent(SendEmailRequest message) {

        emailSender.sendMessage(message.getRecipient(), message.getSubject(), message.getBody());
    }
}
