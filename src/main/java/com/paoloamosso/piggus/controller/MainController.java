/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

// This class is needed to control the service and manage the views for all the application

/*
 * Main controller for web requests
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.model.Deadline;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class MainController {

    // == fields ==
    private ExpenseService expenseService;
    private UserService userService;
    private DeadlineService deadlineService;

    // == constructor ==
    @Autowired
    public MainController(ExpenseService expensesService, UserService userService, DeadlineService deadlineService) {
        this.expenseService = expensesService;
        this.userService = userService;
        this.deadlineService = deadlineService;
    }

    // == handler methods ==
    // ==== HOME ====
    @RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        LocalDate localDate = LocalDate.now();
        user.setLastLogin(localDate);
        userService.saveUser(user);

        // Configuration alert
        modelAndView.addObject("budget",user.getMonthlyBudget());

        // Creating the expenses list and pushing them
        List<Expense> fixedExpenses = expenseService.getFixedExpensesByDate(user,localDate);
        Double totalCostFixedExpenses = expenseService.getTotalExpenses(fixedExpenses);
        List<Expense> variableExpenses = expenseService.getVariableExpensesByDate(user,localDate);
        Double totalCostVariableExpenses = expenseService.getTotalExpenses(variableExpenses);
        modelAndView.addObject("fixedExpenses", fixedExpenses);
        modelAndView.addObject("totalCostFixedExpenses", expenseService.round(totalCostFixedExpenses,2));
        modelAndView.addObject("variableExpenses", variableExpenses);
        modelAndView.addObject("totalCostVariableExpenses" , expenseService.round(totalCostVariableExpenses,2));

        // Sending user kpis
        modelAndView.addObject("moneyLeft",expenseService.round(user.getMonthlyBudget() - user.getMonthlySaving()-totalCostFixedExpenses-totalCostVariableExpenses,2));
        modelAndView.addObject("percentageMoneyLeft",expenseService.round((user.getMonthlyBudget() - user.getMonthlySaving()-totalCostFixedExpenses-totalCostVariableExpenses)/(user.getMonthlyBudget() - user.getMonthlySaving()),2));
        modelAndView.addObject("saving",user.getMonthlySaving());

        // Creating the deadlines list and pushing them
        Map<String, List<Deadline>> deadlines = deadlineService.getDeadlines(user, localDate);
        modelAndView.addObject("deadlines", deadlines);

        // Quick add a new expense
        Expense expense = new Expense();
        expense.setNumberMonths(1);
        expense.setLocalDate(LocalDate.now());
        modelAndView.addObject("expenseCategoryList", user.getExpenseType());
        modelAndView.addObject("newQuickExpense",expense);
        modelAndView.setViewName("home");
        return modelAndView;
    }

    // ==== Privacy & Cookie Policy
    @RequestMapping(value="/privacy-cookie-policy", method = RequestMethod.GET)
    public ModelAndView policyPrivacyCookie(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("privacy-cookie-policy");
        return modelAndView;
    }
}
