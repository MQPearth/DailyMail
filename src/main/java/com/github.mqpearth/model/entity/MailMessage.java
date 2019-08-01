package com.github.mqpearth.model.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 对SimpleMailMessage进行一层封装
 */
@Component
public class MailMessage {

    @Value("${spring.mail.username}")
    private String fromMail;

    private MailMessage() {
    }

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 简单邮箱消息工厂类
     *
     * @param toMail  收信人账号
     * @param subject 邮件主题
     * @param text    邮件内容
     * @return
     */
    public MimeMessage create(String toMail, String subject, String text) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(fromMail);
        helper.setTo(toMail);
        helper.setSubject(subject);
        helper.setText(text, true); //支持html

        return mimeMessage;
    }


}
