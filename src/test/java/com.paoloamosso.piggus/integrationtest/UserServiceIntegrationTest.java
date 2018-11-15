package com.paoloamosso.piggus.integrationtest;

import com.paoloamosso.piggus.PiggusApplication;
import com.paoloamosso.piggus.dao.RoleRepository;
import com.paoloamosso.piggus.model.Role;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.UserService;
import com.paoloamosso.piggus.util.SetupClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = PiggusApplication.class)
@TestPropertySource(
        locations = "classpath:application-integration-test.properties")
public class UserServiceIntegrationTest extends SetupClass {

    // == fields ==
    @Autowired
    private UserService userService;

    // == init ==
    @Before
    public void setup(){
        super.setup();
    }

    // == test methods ==
    @Test
    public void givenNewEmployeeEncrypted_whenCheckedOnDB_thenFound(){
        User user = new User();
        user.setEmail("email2@email.it");
        user.setPassword("password");
        userService.createUser(user);
        User found = userService.findUserByDecryptedEmail("email2@email.it");
        assertNotNull(found.getEmail());
    }
}
