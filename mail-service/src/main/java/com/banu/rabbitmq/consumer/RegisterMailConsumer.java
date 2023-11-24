package com.banu.rabbitmq.consumer;

import com.banu.rabbitmq.model.RegisterMailModel;
import com.banu.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterMailConsumer {

    private final MailSenderService mailSenderService;

    @RabbitListener(queues = "${rabbitmq.register-mail-queue}") //deserialize ettik
    public void sendActivationCode(RegisterMailModel model){
        log.info("User {}", model.toString());
        mailSenderService.sendMail(model);
    }

}
