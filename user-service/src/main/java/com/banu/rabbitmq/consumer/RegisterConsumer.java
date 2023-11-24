package com.banu.rabbitmq.consumer;

import com.banu.rabbitmq.model.RegisterModel;
import com.banu.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // Console'a log çıktısı vermek için kullanılan bir kütüphane
public class RegisterConsumer {

    private final UserProfileService userProfileService;

    //servisçağırıp onu tetikliyoruz. feign de controllerda end point etikliyoduk

    @RabbitListener(queues = "${rabbitmq.queue-register}")
    public void createNewUser(RegisterModel model){ //request dto gibi iki taraftada veriyorum
        log.info("Model {}",model.toString());
        userProfileService.createUserWithRabbitMq(model);
    }
}
