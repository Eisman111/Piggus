/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * This is the bridge between the repository and the controller
 */

package com.paoloamosso.piggus.service;

import com.paoloamosso.piggus.dao.ExpenseRepository;
import com.paoloamosso.piggus.model.Expense;

import com.paoloamosso.piggus.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service("expenseService")
public class ExpenseService {

    // == fields ==
    @Autowired
    @Qualifier("expenseRepository")
    private ExpenseRepository expenseRepository;
    private Calendar cal = Calendar.getInstance();

    // == public methods ==
    public void addExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    public void removeExpense(Expense expense) {
        expenseRepository.delete(expense);
    }

    public Expense getExpense(int expenseID) {
        return expenseRepository.findExpenseById(expenseID);
    }

    public List<Expense> getExpenses (User user) {
        return expenseRepository.findExpensesByUser(user);
    }

    // 1. Take the list of expense for that user
    // 2. Filter the list:
    // -- Not fixed cost
    // -- Same month && year of the cost
    // -- The cost has effect on the month
    // 3. Divide the cost on the number of months
    // 4. return the list
    public List<Expense> getVariableExpensesByDate (User user, LocalDate localDate) {
        List<Expense> allExpenses = expenseRepository.findExpensesByUser(user);
        return allExpenses.stream()
                .filter( e -> !e.isFixedCost()
                                && ((e.getLocalDate().getMonthValue() == localDate.getMonthValue()
                                && e.getLocalDate().getYear() == localDate.getYear())
                                || (localDate.getMonthValue() >= e.getLocalDate().getMonthValue()
                                && localDate.getMonthValue() <= (e.getLocalDate().getMonthValue() + e.getNumberMonths())
                                && localDate.getYear() == e.getLocalDate().getYear())
                                || (localDate.getMonthValue() <= (e.getLocalDate().getMonthValue() + e.getNumberMonths() - 12)
                                && (localDate.getYear() == (e.getLocalDate().getYear() + 1)))))
                .map(e -> {e.setCost(round(e.getCost() / e.getNumberMonths(),2)); return e;})
                .collect(Collectors.toList());
    }

    // 1. Take the list of expense for that user
    // 2. Filter the list:
    // -- Only fixed cost
    // -- Same month && year of the cost
    // -- The cost has effect on the month
    // 3. Divide the cost on the number of months
    // 4. return the list
    public List<Expense> getFixedExpensesByDate (User user, LocalDate localDate) {
        List<Expense> allExpenses = expenseRepository.findExpensesByUser(user);
        return allExpenses.stream()
                .filter(e -> e.isFixedCost()
                            && ((e.getLocalDate().getMonthValue() == localDate.getMonthValue()
                            && e.getLocalDate().getYear() == localDate.getYear())
                            || (localDate.getMonthValue() >= e.getLocalDate().getMonthValue()
                            && localDate.getMonthValue() <= (e.getLocalDate().getMonthValue() + 12)
                            && localDate.getYear() == e.getLocalDate().getYear())
                            || (localDate.getMonthValue() <= ((e.getLocalDate().getMonthValue() + 12) - 12)
                            && (localDate.getYear() == (e.getLocalDate().getYear() + 1)))))
                .map(e -> {e.setCost(round(e.getCost() / e.getNumberMonths(),2)); return e;})
                .collect(Collectors.toList());
    }

    public Double getTotalExpenses (List<Expense> variableExpenses) {
        return variableExpenses.stream()
                .mapToDouble(e -> e.getCost())
                .sum();
    }

    // TODO refactor Priority 1: It's not efficient at all and it's crazy!
    public Map<String,String> getActiveMonthsForFilter (User user) {
        Map<String,String> monthsYear = new LinkedHashMap<>();
        List<Expense> variableExpenses = expenseRepository.findExpensesByUserOrderByLocalDateAsc(user);
        variableExpenses.stream()
                .forEach(e -> {
                    int month = e.getLocalDate().getMonthValue();
                    int year = e.getLocalDate().getYear();
                    LocalDate localDate = e.getLocalDate();
                    for(int i = 1; i <= e.getNumberMonths(); i++) {
                        localDate = LocalDate.of(year,month,1);
                        if (month < 10) {
                            monthsYear.put(year + "-0" + month + "-01", year + " " + localDate.getMonth());
                        } else {
                            monthsYear.put(year + "-" + month + "-01", year + " " + localDate.getMonth());
                        }
                        month++;
                        if (month > 12) {
                            month -= 12;
                            year++;
                        }
                    }
                });
        return monthsYear;
    }

    // Method do synthesize to 2 decimals a double
    // TODO improvement Priority 4: understand if this is the best way to manage this
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
