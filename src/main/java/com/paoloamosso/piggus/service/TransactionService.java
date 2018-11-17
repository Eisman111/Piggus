/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * This is the bridge between the repository and the controller
 */

package com.paoloamosso.piggus.service;

import com.paoloamosso.piggus.dao.TransactionRepository;
import com.paoloamosso.piggus.model.Transaction;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.util.Periodicity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("transactionService")
public class TransactionService {

    // == fields ==
    @Autowired
    private TransactionRepository transactionRepository;

    // == public methods ==
    public void addTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void archiveTransaction(Transaction transaction) {
        transaction.setIsArchived(true);
        transactionRepository.save(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    public Transaction getTransaction(int transactionID) {
        return transactionRepository.findTransactionById(transactionID);
    }

    public List<Transaction> getCurrentMonthTransactions (User user) {
        LocalDate startMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return transactionRepository.findByMonthAndNotArchived(user, startMonth, endMonth);
    }

    public List<Transaction> getMonthTransactions (User user, LocalDate localDate) {
        LocalDate startMonth = localDate.withDayOfMonth(1);
        LocalDate endMonth = localDate.withDayOfMonth(localDate.lengthOfMonth());
        return transactionRepository.findByMonthAndNotArchived(user,startMonth,endMonth);
    }

    public List<Transaction> findByRecurrentTransactionNotArchivedForMonth (LocalDate startmonth, LocalDate endMonth, int recurrentFactor) {
        return transactionRepository.findByRecurrentTransactionNotArchivedForMonth(startmonth,endMonth,recurrentFactor);
    }

    // We are going to use this method to populate the dropdown to select the period in the list of transaction
    // We are using a map so that we can pass a key value for the drop down
    // TODO refactor Priority 2: Find a better way to do this, this is is horrible!
    public Map<String,String> findMonthListWithTransactions (User user) {
        List<String> rawMonthYear = transactionRepository.findMonthListWithTransactions(user.getIdUser());
        Map<String,String> cleanMonthYear = new LinkedHashMap<>();
        rawMonthYear.forEach(my -> {
            String year = my.substring(0,4);
            String month = my.substring(4,6);
            cleanMonthYear.put(year + "-" + month + "-01",Periodicity.convertMonthAndYear(month,year));
        });
        return cleanMonthYear;
    }

//    // 1. Take the list of transaction for that user
//    // 2. Filter the list:
//    // -- Not fixed cost
//    // -- Same month && year of the cost
//    // -- The cost has effect on the month
//    // 3. Divide the cost on the number of months
//    // 4. return the list
//    public List<Transaction> getVariableExpensesByDate (User user, LocalDate localDate) {
//        List<Transaction> allExpens = transactionRepository.findExpensesByUser(user);
//        return allExpens.stream()
//                .filter( e -> !e.isFixedCost()
//                                && ((e.getLocalDate().getMonthValue() == localDate.getMonthValue()
//                                && e.getLocalDate().getYear() == localDate.getYear())
//                                || (localDate.getMonthValue() >= e.getLocalDate().getMonthValue()
//                                && localDate.getMonthValue() <= (e.getLocalDate().getMonthValue() + e.getNumberMonths())
//                                && localDate.getYear() == e.getLocalDate().getYear())
//                                || (localDate.getMonthValue() <= (e.getLocalDate().getMonthValue() + e.getNumberMonths() - 12)
//                                && (localDate.getYear() == (e.getLocalDate().getYear() + 1)))))
//                .map(e -> {e.setCost(round(e.getCost() / e.getNumberMonths(),2)); return e;})
//                .collect(Collectors.toList());
//    }
//
//    // 1. Take the list of transaction for that user
//    // 2. Filter the list:
//    // -- Only fixed cost
//    // -- Same month && year of the cost
//    // -- The cost has effect on the month
//    // 3. Divide the cost on the number of months
//    // 4. return the list
//    public List<Transaction> getFixedExpensesByDate (User user, LocalDate localDate) {
//        List<Transaction> allExpens = transactionRepository.findExpensesByUser(user);
//        return allExpens.stream()
//                .filter(e -> e.isFixedCost()
//                            && ((e.getLocalDate().getMonthValue() == localDate.getMonthValue()
//                            && e.getLocalDate().getYear() == localDate.getYear())
//                            || (localDate.getMonthValue() >= e.getLocalDate().getMonthValue()
//                            && localDate.getMonthValue() <= (e.getLocalDate().getMonthValue() + 12)
//                            && localDate.getYear() == e.getLocalDate().getYear())
//                            || (localDate.getMonthValue() <= ((e.getLocalDate().getMonthValue() + 12) - 12)
//                            && (localDate.getYear() == (e.getLocalDate().getYear() + 1)))))
//                .map(e -> {e.setCost(round(e.getCost() / e.getNumberMonths(),2)); return e;})
//                .collect(Collectors.toList());
//    }
//
//    public Double getTotalExpenses (List<Transaction> variableExpens) {
//        return variableExpens.stream()
//                .mapToDouble(e -> e.getCost())
//                .sum();
//    }

    // Method do synthesize to 2 decimals a double
    // TODO improvement Priority 4: understand if this is the best way to manage this, also find another way to check
    public static double round(double value, int places) {
        if (value>-10000) {
            if (places < 0) throw new IllegalArgumentException();

            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } else {
            return value;
        }
    }
}
