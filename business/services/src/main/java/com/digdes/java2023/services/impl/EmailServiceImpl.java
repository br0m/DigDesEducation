package com.digdes.java2023.services.impl;

import com.digdes.java2023.dto.enums.TaskStatus;
import com.digdes.java2023.model.Project;
import com.digdes.java2023.model.Task;
import com.digdes.java2023.model.TeamMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.ST;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl {

    private final JavaMailSender emailSender;
    private final ST messageTemplate;

    public void sendMessage(Task task, boolean fakeEmail) {
        log.info("Preparing email message");
        fillMessageTemplate(task.getResponsibleMember(), task.getProject(), task.getTitle(), task.getDescription(), task.getDeadline(), task.getStatus(), task.getAuthor(), task.getCreationDate());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("korostelev.sergey.a@gmail.com");
        message.setSubject("New task assigned");

        String realEmail = task.getResponsibleMember().getMember().getEmail();
        String to = fakeEmail ? "br0m2@yandex.ru" : realEmail;
        String text = fakeEmail ? realEmail + "\n" + messageTemplate.render() : messageTemplate.render();

        message.setTo(to);
        message.setText(text);
        log.info("Message ready, sending...");
        log.debug(message.toString());

        try {
            emailSender.send(message);
            log.info("Email sent successfully");
        } catch (MailException e) {
            log.error(e.getMessage());
        }
    }

    private void fillMessageTemplate(TeamMember responsibleMember, Project project, String title, String description, LocalDateTime deadline, TaskStatus status, TeamMember author, LocalDateTime creationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        messageTemplate.add("name", responsibleMember.getMember().getFirstName() + " " + responsibleMember.getMember().getLastName());
        messageTemplate.add("codename", project.getCodename());
        messageTemplate.add("title", title);
        messageTemplate.add("description", description);
        messageTemplate.add("deadline", deadline.format(formatter));
        messageTemplate.add("status", status);
        messageTemplate.add("author", author.getMember().getFirstName() + " " + author.getMember().getLastName());
        messageTemplate.add("creationDate", creationDate.format(formatter));
    }
}
