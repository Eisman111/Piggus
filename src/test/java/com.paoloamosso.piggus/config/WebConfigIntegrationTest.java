package com.paoloamosso.piggus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.test.context.TestPropertySource;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@TestConfiguration
@TestPropertySource(
        locations = "classpath:application-integration-test.properties")
public class WebConfigIntegrationTest {

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
}
