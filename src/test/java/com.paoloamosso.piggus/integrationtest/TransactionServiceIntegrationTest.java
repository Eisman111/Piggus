package com.paoloamosso.piggus.integrationtest;

import com.paoloamosso.piggus.PiggusApplication;
import com.paoloamosso.piggus.model.Transaction;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.TransactionService;
import com.paoloamosso.piggus.service.UserService;
import com.paoloamosso.piggus.util.SetupClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = PiggusApplication.class)
public class TransactionServiceIntegrationTest extends SetupClass{

    // == fields ==
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    // == init ==
    @Before
    public void setup(){
        super.setup();
    }

    // == test methods ==
    @Test
    public void givenUser_whenAddingATransaction_thenVerifyMandatoryFieldsNotNull() {
        User user = userService.findUserByDecryptedEmail(TESTEMAIL);
        List<Transaction> resultedTransaction = transactionService.getCurrentMonthTransactions(user);
        resultedTransaction.forEach(t -> assertThat(t.getId()).isNotNull());
    }

    @Test
    public void givenUser_whenSearchingForMonthlyTransaction_thenFindSome(){
        User user = userService.findUserByDecryptedEmail(TESTEMAIL);
        List<Transaction> resultedTransactionForMonth = transactionService.getCurrentMonthTransactions(user);
        LocalDate startMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        resultedTransactionForMonth.forEach(t -> assertThat(t.getLocalDate()).isBetween(startMonth,endMonth));
    }

    // This test works because on the setup there are three transaction, if you change any of them you have to update this
    @Test
    public void givenUser_whenSearchingForMonthsWithTransactions_thenCreateOrderedPeriodList(){
        User user = userService.findUserByDecryptedEmail(TESTEMAIL);
        Map<String,String> monthYear = transactionService.findMonthListWithTransactions(user);

        // Keys
        List<String> keyList = new ArrayList<>();
        for (String periodKey : monthYear.keySet()) {
            keyList.add(periodKey);
        }

        // Values
        String firstPeriod = LocalDate.now().getMonth() + " " + LocalDate.now().getYear();
        String secondPeriod = LocalDate.now().minusMonths(1).getMonth() + " " + LocalDate.now().minusMonths(1).getYear();
        String thirdPeriod = LocalDate.now().minusMonths(12).getMonth() + " " + LocalDate.now().minusMonths(12).getYear();

        assertThat(monthYear.get(keyList.get(0))).isEqualToIgnoringCase(firstPeriod);
        assertThat(monthYear.get(keyList.get(1))).isEqualToIgnoringCase(secondPeriod);
        assertThat(monthYear.get(keyList.get(2))).isEqualToIgnoringCase(thirdPeriod);
    }
}
