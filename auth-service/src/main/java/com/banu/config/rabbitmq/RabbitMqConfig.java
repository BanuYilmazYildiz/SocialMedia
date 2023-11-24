package com.banu.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.exchange-auth}")
    private String exchange;
    @Value("${rabbitmq.register-key}")
    private String registerBindingKey;
    @Value("${rabbitmq.queue-register}")
    private String queueNameRegister;

    @Value("${rabbitmq.register-mail-key}")
    private String registerMailBindingKey;

    @Value("${rabbitmq.register-mail-queue}")
    private String registerMailQueue;

    @Bean // spring yönetebilsin
    public DirectExchange exchangeAuth(){
        return new DirectExchange(exchange);
    }
    @Bean
    public Queue registerQueue(){
        return new Queue(queueNameRegister); //registert sıraısına gireceğimi söylüyor
    }
    @Bean
    public Binding bindingRegister(final Queue registerQueue, final DirectExchange exchangeAuth){ //bağlayıcı anahtar olusturuyor
        //register queue'yu exchange autha register binding key ile bağlayacğaız
        return BindingBuilder.bind(registerQueue).to(exchangeAuth).with(registerBindingKey);
    }

    @Bean
    public Queue registerMailQueue(){
        return new Queue(registerMailQueue); //register sıraısına gireceğimi söylüyor
    }
    @Bean
    public Binding bindingRegisterMail(final Queue registerMailQueue, final DirectExchange exchangeAuth){ //bağlayıcı anahtar olusturuyor
        //register queue'yu exchange autha register binding key ile bağlayacğaız
        return BindingBuilder.bind(registerMailQueue).to(exchangeAuth).with(registerMailBindingKey);
    }
}
