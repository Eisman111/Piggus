package com.paoloamosso.piggus.integrationtest;

import com.paoloamosso.piggus.PiggusApplication;
import com.paoloamosso.piggus.model.Transaction;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = PiggusApplication.class)
public class TransactionJobIntegrationTest extends SetupClass {

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
    public void giveUserAndRecurrentTransaction_whenSearchedForLastMonth_thenFound(){
        LocalDate startMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
        List<Transaction> resultedTransactionForMonth = transactionService
                .findByRecurrentTransactionNotArchivedForMonth(startMonth,endMonth,1);
        assertThat(resultedTransactionForMonth).size().isEqualTo(1);
    }

    //TODO why this does not work?s
//    @Test
//    public void givenTime_whenJobTriggerFire_thenJobWork() throws SchedulerException{
//        SchedulerFactory factory = new StdSchedulerFactory();
//        Scheduler scheduler = factory.getScheduler();
//
//        //Create a new Job
//        JobKey jobKey = JobKey.jobKey("myRecurrentTransactionJob", "TransactionGroup");
//        JobDetail job =JobBuilder.newJob(RecurrentTransactionsJob.class).withIdentity(jobKey).storeDurably().build();
//
//        //Register this job to the scheduler
//        scheduler.addJob(job, true);
//
//        //Immediately fire the Job MyJob.class
//        scheduler.triggerJob(jobKey);
//
//        User user = userService.findUserByDecryptedEmail(TESTEMAIL);
//        List<Transaction> transactions = transactionService.getCurrentMonthTransactions(user);
//        assertThat(transactions).size().isEqualTo(3);
//    }
}
