package dev.thiagorodrigues.livraria.infra.providers.mail;

public interface MailService {

    void sendMail(String recipient, String subject, String content);

}
