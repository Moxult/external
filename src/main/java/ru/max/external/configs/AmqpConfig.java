package ru.max.external.configs;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.max.external.application.messaging.request.RegistrationRequest;
import ru.max.external.application.messaging.request.SendEmailRequest;
import ru.max.external.application.messaging.response.RegistrationResponse;
import ru.max.external.configs.properties.RegistrationProperties;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Configuration
@EnableConfigurationProperties(RegistrationProperties.class)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AmqpConfig {

    static String DLQ_ROUTING_KEY = "task.dlq.external.system";
    static String DLQ_BINDING_KEY = "task.dlq.#";
    static String ROUTING_KEY = "task.routing.out.#";
    static String ROUTING_KEY_EVENT = "task.routing.event.#";

    @Bean
    Queue queue(@NonNull RegistrationProperties registrationProperties) {
        return QueueBuilder.durable(registrationProperties.getExternalSystem().getQueue())
                .ttl((int) registrationProperties.getDeadLetter().getDlqTimeout().toMillis())
                .deadLetterExchange(registrationProperties.getDeadLetter().getExchangeName())
                .deadLetterRoutingKey(DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    Queue queueEvent(@NonNull RegistrationProperties registrationProperties) {
        return QueueBuilder.durable(registrationProperties.getExternalSystem().getQueueEvent())
                .build();
    }

    @Bean
    public Queue dlqQueue(@NonNull RegistrationProperties registrationProperties) {

        return QueueBuilder.durable(registrationProperties.getDeadLetter().getQueueName())
                .lazy()
                .build();
    }

    @Bean
    public TopicExchange exchange(@NonNull RegistrationProperties registrationProperties) {
        return new TopicExchange(registrationProperties.getExternalSystem().getExchange());
    }

    @Bean
    public TopicExchange dlqExchange(@NonNull RegistrationProperties registrationProperties) {

        return new TopicExchange(registrationProperties.getDeadLetter().getExchangeName());
    }

    @Bean
    public Binding binding(@NonNull Queue queue,
                           @NonNull TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingEvent(@NonNull Queue queueEvent,
                                @NonNull TopicExchange exchange) {
        return BindingBuilder.bind(queueEvent).to(exchange).with(ROUTING_KEY_EVENT);
    }

    @Bean
    public Binding dlqQueueBinding(@NonNull Queue dlqQueue,
                                   @NonNull TopicExchange dlqExchange) {

        return BindingBuilder.bind(dlqQueue)
                .to(dlqExchange)
                .with(DLQ_BINDING_KEY);
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);

        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        jsonMessageConverter.setClassMapper(classMapper());
        return jsonMessageConverter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("ru.max.task.application.messaging.request.RegistrationRequest", RegistrationRequest.class);
        idClassMapping.put("ru.max.task.application.messaging.response.RegistrationResponse", RegistrationResponse.class);
        idClassMapping.put("ru.max.task.application.messaging.request.SendEmailRequest", SendEmailRequest.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;

    }
}
