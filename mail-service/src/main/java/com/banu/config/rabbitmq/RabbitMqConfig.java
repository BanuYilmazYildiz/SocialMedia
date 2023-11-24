package com.banu.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.register-mail-queue}")
    private String registerMailQueue;

    @Bean
    public Queue registerMailQueue() {
        return new Queue(registerMailQueue);
    }

    //tanımlıyoruz ki rabbitlistener dinleyebilsin, queue tanımlıyorum servis ayağa kalktığında servis hata vermiyor
}
