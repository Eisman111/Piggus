package com.paoloamosso.piggus.repository;

import static org.assertj.core.api.Assertions.*;
import com.paoloamosso.piggus.dao.UserRepository;
import com.paoloamosso.piggus.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    // == fields ==
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    // == tests ==
    @Test
    public void givenUser_whenFindByEmail_thenReturnUser() {
        //given
        User user = new User();
        user.setEmail("testingDecrypted@test.it");
        user.setPassword("testing");
        user.setRegistrationDate(LocalDate.now());
        user.setLastLogin(LocalDate.now());
        entityManager.persist(user);
        entityManager.flush();

        //when
        User found = userRepository.findByEmail(user.getEmail());

        //then
        assertThat(found.getEmail().equals(user.getEmail()));
    }
}
