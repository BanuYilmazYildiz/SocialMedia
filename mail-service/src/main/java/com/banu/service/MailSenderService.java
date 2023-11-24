package com.banu.service;

import com.banu.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final JavaMailSender javaMailSender;

    public void sendMail(RegisterMailModel model){
        SimpleMailMessage mailMessage = new SimpleMailMessage(); //özelleştirmeleri buradan yapacağız. başlık, textte ne yazacak
        mailMessage.setFrom("${Java11MailUsurname}");
        mailMessage.setTo(model.getEmail());
        mailMessage.setSubject("SocialMediaApp Aktivasyon Kodunuz");
        mailMessage.setText("Değerli "+model.getUsername() +" Hesap doğrulama kodunuz : "+model.getActivationCode());
        javaMailSender.send(mailMessage);
    }

}
