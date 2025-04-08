package com.vazant.logix.orders.application.service.shared.email;

import com.vazant.logix.orders.presentation.exception.EmailDeliveryException;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  public <T> void sendEmail(String to, String subject, String templatePath, T model) {
    String html = render(templatePath, model);
    sendHtmlEmail(to, subject, html);
  }

  private <T> String render(String templatePath, T model) {
    StringOutput output = new StringOutput();
    templateEngine.render(templatePath, model, output);
    return output.toString();
  }

  private void sendHtmlEmail(String to, String subject, String html) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(html, true);
      mailSender.send(message);
    } catch (MessagingException e) {
      throw throw new EmailDeliveryException(to, e);
    }
  }
}
