package com.paoloamosso.piggus.util;

import com.paoloamosso.piggus.PiggusApplication;
import com.paoloamosso.piggus.dao.RoleRepository;
import com.paoloamosso.piggus.model.Role;
import com.paoloamosso.piggus.model.Transaction;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.TransactionService;
import com.paoloamosso.piggus.service.UserService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = PiggusApplication.class)
@TestPropertySource(
        locations = "classpath:application-integration-test.properties")
public abstract class SetupClass {

    // == fields ==
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    public static String TESTEMAIL = "email@email.it";

    @Before
    public void setup(){
        if (roleRepository.findByRole("USER") == null) {
            roleRepository.save(new Role(1,"USER"));
        }
        User user = new User();
        user.setEmail("email@email.it");
        user.setPassword("password");
        userService.createUser(user);

        Transaction t1 = new Transaction();
        t1.setMoneyTransaction(0.0);
        t1.setTitle("Test");
        t1.setLocalDate(LocalDate.now());
        t1.setUser(user);
        t1.setIsRecurrent(false);
        t1.setTransactionType("Groceries");
        transactionService.addTransaction(t1);

        Transaction t2 = new Transaction();
        t2.setMoneyTransaction(0.0);
        t2.setTitle("Test");
        t2.setLocalDate(LocalDate.now().minusMonths(1));
        t2.setUser(user);
        t2.setIsRecurrent(true);
        t2.setIsArchived(false);
        t2.setRecurrentFactor(1);
        t2.setTransactionType("Groceries");
        transactionService.addTransaction(t2);

        Transaction t3 = new Transaction();
        t3.setMoneyTransaction(0.0);
        t3.setTitle("Test");
        t3.setLocalDate(LocalDate.now().minusMonths(12));
        t3.setUser(user);
        t3.setIsRecurrent(true);
        t3.setIsArchived(false);
        t3.setRecurrentFactor(2);
        t3.setTransactionType("Groceries");
        transactionService.addTransaction(t3);
    }
}
