package com.example.Email.Service.Imp;

import com.example.Email.Dto.DataMailDto;
import com.example.Email.Service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
@RequiredArgsConstructor
public class MailServiceImp implements MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    @Override
    public void sendHtmlEmail(DataMailDto dataMailDto, String templateName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context thymeLeafContext = new Context();
        thymeLeafContext.setVariables(dataMailDto.getProps());

        String html = springTemplateEngine.process(templateName, thymeLeafContext);

        helper.setTo(dataMailDto.getTo());
        helper.setSubject(dataMailDto.getSubject());
        helper.setText(html, true);

        javaMailSender.send(message);
    }
}
