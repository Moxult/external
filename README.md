# External - тестовое задание (ответная система)

# Задача системы
Обработка запросов на регистрацию из внешней системы и обработка запросов на отправку email сообщений

## Для разработчика
### Конфигурация
В корне проекта находится файл `application.properties` в котором указан пример конфигурации для запуска.
В файле указаны параметры подключения к RabbitMQ и серверу smtp.
Логин и пароль для подключения к серверу smtp`spring.mail.username`
`spring.mail.password` узнать у разработчика

### Обязательная конфигурация
Любой конфиг может быть задан не только в файле для конфигурации, но и в переменных окружениях.
```yaml
server.port = 8282

spring.profiles.active=dev

# Настройка подключения к RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

logging.level.ru.max.task=DEBUG

# Настройка подключения к email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
