package com.andrew.MyTicket.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration mail sender been
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Configuration
public class MailConfig {
    /**
     * Sender email host
     */
    @Value("${spring.mail.host}")
    private String host;

    /**
     * Sender email username
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * Sender email password
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Sender email port
     */
    @Value("${spring.mail.port}")
    private int port;

    /**
     * Sender email protocol
     */
    @Value("${spring.mail.protocol}")
    private String protocol;

    /**
     * Sender email debug
     */
    @Value("${mail.debug}")
    private String debug;

    /**
     * Sets the mail sender configuration
     *
     * @return Instance of JavaMailSender
     */
    @Bean
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);

        Properties javaMailProperties = javaMailSender.getJavaMailProperties();

        javaMailProperties.setProperty("mail.transport.protocol", protocol);
        javaMailProperties.setProperty("mail.debug", debug);

        return javaMailSender;
    }
}
