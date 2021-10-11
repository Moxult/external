package ru.max.external.configs.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.Duration;

@Validated
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "external.registration")
public class RegistrationProperties {

    @Valid
    @NotNull ExternalSystem externalSystem = new ExternalSystem();

    @Valid
    @NotNull DeadLetter deadLetter = new DeadLetter();

    @Validated
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ExternalSystem {

        @NotEmpty
        String exchange = "task.exchange";

        @NotBlank
        String queue = "task.queue.out";

        @NotBlank
        String queueEvent = "task.queue.event";
    }

    @Validated
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class DeadLetter {

        @NotBlank
        String exchangeName = "task.exchange";

        @NotBlank
        String queueName = "task.dlq";

        @NotNull
        Duration dlqTimeout = Duration.ofMinutes(2);
    }
}
