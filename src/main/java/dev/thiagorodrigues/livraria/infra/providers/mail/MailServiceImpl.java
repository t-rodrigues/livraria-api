package dev.thiagorodrigues.livraria.infra.providers.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("prod")
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${from.email.address}")
    private String fromEmailAddress;

    @Override
    @Async
    public void sendMail(String recipient, String subject, String content) {
        var email = new SimpleMailMessage();

        email.setFrom(String.format("Contato <%s>", fromEmailAddress));
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(content);

        javaMailSender.send(email);
    }

}
