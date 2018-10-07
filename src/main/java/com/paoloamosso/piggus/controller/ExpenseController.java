/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * The expense controller is necessary to manage the web requests
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.model.Expense;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.DeadlineService;
import com.paoloamosso.piggus.service.ExpenseService;
import com.paoloamosso.piggus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ExpenseController {

    // == fields ==
    private ExpenseService expenseService;
    private UserService userService;

    // == constructor ==
    @Autowired
    public ExpenseController(ExpenseService expensesService, UserService userService, DeadlineService deadlineService) {
        this.expenseService = expensesService;
        this.userService = userService;
    }

    // == handler methods ==
    // ==== VIEW EXPENSES ====
    @RequestMapping(value="/expense/list", method = RequestMethod.GET)
    public ModelAndView listExpenses (@RequestParam(value="date",required = false, defaultValue = "-1") String date) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        List<Expense> fixedExpenses = new ArrayList<>();
        List<Expense> variableExpenses = new ArrayList<>();
        LocalDate localDate;
        try {
            if (!date.equals("-1")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                localDate = LocalDate.parse(date,formatter);
            } else {
                localDate = LocalDate.now();
            }
            fixedExpenses = expenseService.getFixedExpensesByDate(user, localDate);
            variableExpenses = expenseService.getVariableExpensesByDate(user,localDate);
        } catch (NullPointerException e) {
            log.warn("Something went wrong in the expenses lists date controller");
        }
        modelAndView.addObject("fixedExpenses",fixedExpenses);
        modelAndView.addObject("variableExpenses",variableExpenses);
        modelAndView.addObject("datesFilters", expenseService.getActiveMonthsForFilter(user));
        modelAndView.setViewName("expense/list");
        return modelAndView;
    }

    // ==== ADD/EDIT EXPENSE ====
    @RequestMapping(value="expense/view", method = RequestMethod.GET)
    public ModelAndView addEditExpense (@RequestParam(value="expenseID",required = false, defaultValue = "-1") int expenseId) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Expense expense = expenseService.getExpense(expenseId);
        if (!user.getExpenses().contains(expense)) {
            expense = new Expense();
        }
        modelAndView.addObject("expense",expense);
        modelAndView.addObject("expenseCategoryList", user.getExpenseType());
        modelAndView.setViewName("expense/view");
        return modelAndView;
    }
    @RequestMapping(value = "/update-expense", method = RequestMethod.POST)
    public ModelAndView processExpense(@ModelAttribute("expense") Expense expense) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        expense.setUser(user);
        expenseService.addExpense(expense);
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    // ==== REMOVE EXPENSE ====
    @RequestMapping(value="/remove-expense", method=RequestMethod.GET)
    public ModelAndView removeExpense (@RequestParam(value="expenseID",required = false) int expenseID) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Expense expense = expenseService.getExpense(expenseID);
        if (user.getExpenses().contains(expense)) {
            expenseService.removeExpense(expense);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }
}
