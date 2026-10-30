package com.inmobiliaria.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarFacturaEmail(String destinatario, String asunto, String cuerpo, byte[] pdf, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpo);
            helper.addAttachment(filename, new ByteArrayResource(pdf));
            
            // Envío REAL del correo
            mailSender.send(message);
            System.out.println(">>> [SMTP REAL] Correo enviado con éxito a: " + destinatario + " (Adjunto: " + filename + ")");
        } catch (Exception e) {
            System.err.println(">>> Error al enviar el correo a " + destinatario + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
