package com.banu.rabbitmq.producer;

import com.banu.rabbitmq.model.RegisterModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {

    @Value("${rabbitmq.exchange-auth}")
    private String directExchange;
    @Value("${rabbitmq.register-key}")
    private String registerBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendNewUser(RegisterModel model){ //serialize edip, gerekli (pass information) veriyor
        rabbitTemplate.convertAndSend(directExchange,registerBindingKey,model);
    }
}
