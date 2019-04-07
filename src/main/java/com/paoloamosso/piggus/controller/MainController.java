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
import com.paoloamosso.piggus.model.transaction.DefaultExpense;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.DeadlineService;
import com.paoloamosso.piggus.service.TransactionService;
import com.paoloamosso.piggus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class MainController {

    // == fields ==
    private TransactionService transactionService;
    private UserService userService;
    private DeadlineService deadlineService;

    // == constructor ==
    @Autowired
    public MainController(TransactionService expensesService, UserService userService, DeadlineService deadlineService) {
        this.transactionService = expensesService;
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
        User user = userService.findUserByEncryptedEmail(auth.getName());
        LocalDate localDate = LocalDate.now();
        user.setLastLogin(localDate);
        userService.saveUser(user);

        // Creating the defaultExpense list and pushing them
        List<DefaultExpense> defaultExpenses = transactionService.getCurrentMonthTransactions(user);
        modelAndView.addObject("transactions", defaultExpenses);

        // Creating the deadlines list and pushing them
        Map<String, List<Deadline>> deadlines = deadlineService.getDeadlines(user, localDate);
        modelAndView.addObject("deadlines", deadlines);

        // Quick add a new defaultExpense
        DefaultExpense defaultExpense = new DefaultExpense();
        defaultExpense.setLocalDate(LocalDate.now());
        modelAndView.addObject("expenseCategoryList", user.getTransactionType());
        modelAndView.addObject("newQuickExpense", defaultExpense);
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
