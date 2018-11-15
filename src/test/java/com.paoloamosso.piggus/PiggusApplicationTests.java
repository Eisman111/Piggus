/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * This is the main class of the application, is where everything start
 */

package com.paoloamosso.piggus;

import com.paoloamosso.piggus.config.WebConfig;
import com.paoloamosso.piggus.controller.DeadlineController;
import com.paoloamosso.piggus.service.EmailService;
import com.paoloamosso.piggus.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {WebConfig.class, DeadlineController.class, UserService.class, EmailService.class, JavaMailSenderImpl.class})
public class PiggusApplicationTests {

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}
