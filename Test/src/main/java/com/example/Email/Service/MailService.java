package com.example.Email.Service;

import com.example.Email.Dto.DataMailDto;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendHtmlEmail(DataMailDto dataMailDto, String templateName) throws MessagingException;
}
