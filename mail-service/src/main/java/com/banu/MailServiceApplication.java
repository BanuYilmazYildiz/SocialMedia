package com.banu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class);

    }

    // deneme

//    private final JavaMailSender javaMailSender;
//
//    public MailServiceApplication(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//    @EventListener(ApplicationReadyEvent.class) //uygulama ayağa kalktığında çağırmadan, dinleyerek kendi kendine çalıştırıyor
//    public void sendMail() {
//        SimpleMailMessage mailMessage = new SimpleMailMessage(); //özelleştirmeleri buradan yapacağız. başlık, textte ne yazacak
//        mailMessage.setFrom("${Java11MailUsurname}");
//        mailMessage.setTo("banu.ylm@gmail.com");
//        mailMessage.setSubject("SocialMediaApp Aktivasyon Kodunuz");
//        mailMessage.setText("AR4JI");
//        javaMailSender.send(mailMessage);
//    }

}