package com.paoloamosso.piggus.integrationtest;

import com.paoloamosso.piggus.PiggusApplication;
import com.paoloamosso.piggus.dao.RoleRepository;
import com.paoloamosso.piggus.model.Role;
import com.paoloamosso.piggus.model.Transaction;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.TransactionService;
import com.paoloamosso.piggus.service.UserService;
import com.paoloamosso.piggus.util.SetupClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = PiggusApplication.class)
public class TransactionJobIntegrationTest extends SetupClass {

    // == fields ==
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    // == init ==
    @Before
    public void setup(){

    }

    // == test methods ==
    @Test
    public void giveUserAndRecurrentTransaction_whenSearchedForLastMonth_thenFound(){
        LocalDate startMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
        List<Transaction> resultedTransactionForMonth = transactionService
                .findByRecurrentTransactionNotArchivedForMonth(startMonth,endMonth,1);
        assertThat(resultedTransactionForMonth).size().isEqualTo(1);
    }
}
