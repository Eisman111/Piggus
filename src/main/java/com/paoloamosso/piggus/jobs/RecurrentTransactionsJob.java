//package com.paoloamosso.piggus.jobs;
//
//import com.paoloamosso.piggus.model.transaction.DefaultExpense;
//import com.paoloamosso.piggus.service.DefaultTransactionService;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import java.time.DateTimeException;
//import java.time.LocalDate;
//import java.util.List;
//
//@Slf4j
////public class RecurrentTransactionsJob extends QuartzJobBean {
//
//    // == fields ==
//    private String name;
//    @Autowired
//    private DefaultTransactionService transactionService;
//
//    // == methods ==
//    public void setName (String name) {
//        this.name = name;
//    }
//
//    // To improve performance I'll separate the update for the transaction on the first 27 days with the ones more
//    // problematic from 28 to 31
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        log.info("Recurrent DefaultExpense Job Running");
//        // Monthly check from 1-27
//        LocalDate startMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
//        LocalDate end27Month = LocalDate.now().minusMonths(1).withDayOfMonth(27);
//        try {
//            List<DefaultExpense> firstPartMonthT = transactionService
//                    .findByRecurrentTransactionNotArchivedForMonth(startMonth,end27Month,1);
//            for (DefaultExpense t : firstPartMonthT) {
//                DefaultExpense newTran = new DefaultExpense(t);
//                newTran.setLocalDate(newTran.getLocalDate().plusMonths(1));
//                transactionService.addTransaction(newTran);
//            }
//        } catch (NullPointerException npe) {
//            log.info("Job tried to run but list is empty");
//        }
//
//        // Monthly check from 28-31
//        LocalDate start28Month = LocalDate.now().minusMonths(1).withDayOfMonth(28);
//        LocalDate endMonth = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
//        try {
//            List<DefaultExpense> secondPartMonthT = transactionService
//                    .findByRecurrentTransactionNotArchivedForMonth(start28Month,endMonth,1);
//            for (DefaultExpense t : secondPartMonthT) {
//                DefaultExpense newTran = new DefaultExpense(t);
//                if (newTran.getLocalDate().getDayOfMonth() == 28) {
//                    newTran.setLocalDate(newTran.getLocalDate().plusMonths(1));
//                } else if (newTran.getLocalDate().getDayOfMonth() <= 30){
//                    try {
//                        newTran.setLocalDate(newTran.getLocalDate().plusMonths(1));
//                    } catch (DateTimeException dte) {
//                        newTran.setLocalDate(newTran.getLocalDate().plusMonths(1).withDayOfMonth(28));
//                    }
//                } else {
//                    try {
//                        newTran.setLocalDate(newTran.getLocalDate().plusMonths(1));
//                    } catch (DateTimeException dte) {
//                        try {
//                            newTran.setLocalDate(newTran.getLocalDate().plusMonths(1).withDayOfMonth(30));
//                        } catch (DateTimeException dte2) {
//                            newTran.setLocalDate(newTran.getLocalDate().plusMonths(1).withDayOfMonth(28));
//                        }
//                    }
//                }
//                transactionService.addTransaction(newTran);
//            }
//        } catch (NullPointerException npe) {
//            log.info("Job tried to run but list is empty");
//        }
//
//        // Annual check
//        LocalDate startAnnual = LocalDate.now().minusMonths(12).withDayOfMonth(1);
//        LocalDate endAnnual = LocalDate.now().minusMonths(12).withDayOfMonth(LocalDate.now().lengthOfMonth());
//        try {
//            List<DefaultExpense> annualTransactions = transactionService.findByRecurrentTransactionNotArchivedForMonth(startAnnual,endAnnual,2);
//            for (DefaultExpense t : annualTransactions) {
//                DefaultExpense newTran = new DefaultExpense(t);
//                newTran.setLocalDate(newTran.getLocalDate().plusMonths(12));
//                transactionService.addTransaction(newTran);
//            }
//        } catch (NullPointerException npe) {
//            log.info("Job tried to run but list is empty");
//        }
//    }
//}
