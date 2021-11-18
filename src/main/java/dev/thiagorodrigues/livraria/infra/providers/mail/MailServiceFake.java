package dev.thiagorodrigues.livraria.infra.providers.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({ "dev", "test" })
public class MailServiceFake implements MailService {

    @Override
    public void sendMail(String recipient, String subject, String content) {
        System.out.println(recipient);
        System.out.println(subject);
        System.out.println(content);
    }

}
