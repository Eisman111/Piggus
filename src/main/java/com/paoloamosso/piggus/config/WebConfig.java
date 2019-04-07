/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * In this class we define web configuration and the starting beans
 */

package com.paoloamosso.piggus.config;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Locale;

@Slf4j
@Configuration
@ComponentScan
public class WebConfig  implements WebMvcConfigurer {

    // == Fields ==
    @Value("${textencryptor.password}")
    private String password;
    @Value("${textencryptor.salt}")
    private String salt;

    // == public beans ==
    // Bean necessary to launch security encoding
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    // Bean necessary to manage the configuration of the encryption protocol
    @Bean
    public TextEncryptor textEncryptor  () {
        return Encryptors.delux(password,salt);
    }

    // Trying to manage correctly the localdate variable
    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                return LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DateTimeFormatter.ISO_DATE.format(object);
            }
        };
    }

    // This injection is necessary to manage the session in the cookie.
    // To verify the code
    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
            SessionCookieConfig sessionCookieConfig=servletContext.getSessionCookieConfig();
            sessionCookieConfig.setHttpOnly(true);
        };
    }
//
//    // Quartz Job for recurrent defaultExpenses, it's scheduled on the first day of each month at 1am
//    @Bean
//    public JobDetail transactionJobDetail() {
//        return JobBuilder.newJob(RecurrentTransactionsJob.class).withIdentity("transactionJobDetail")
//                .storeDurably().build();
//    }
//    @Bean
//    public Trigger transactionJobTrigger() {
//        return TriggerBuilder
//                .newTrigger()
//                .forJob(transactionJobDetail())
//                .withIdentity("transactionJobDetail")
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 1 1/1 ? *"))
//                .build();
//    }
//    @Bean
//    public Scheduler transactionJobScheduler() throws SchedulerException{
//        SchedulerFactory factory = new StdSchedulerFactory();
//        Scheduler scheduler = factory.getScheduler();
//        scheduler.scheduleJob(transactionJobDetail(),transactionJobTrigger());
//        scheduler.start();
//        return scheduler;
//    }
}
