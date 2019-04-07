/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * The transaction controller is necessary to manage the web requests
 */

package com.paoloamosso.piggus.controller;

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
public class TransactionController {

    // == fields ==
    private TransactionService transactionService;
    private UserService userService;

    // == constructor ==
    @Autowired
    public TransactionController(TransactionService expensesService, UserService userService, DeadlineService deadlineService) {
        this.transactionService = expensesService;
        this.userService = userService;
    }

    // == handler methods ==
    // ==== VIEW TRANSACTIONS ====
    @RequestMapping(value="/transaction/list", method = RequestMethod.GET)
    public ModelAndView listTransactions (@RequestParam(value="date",required = false, defaultValue = "-1") String date) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        List<DefaultExpense> defaultExpenses = new ArrayList<>();
        LocalDate localDate;
        try {
            if (!date.equals("-1")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                localDate = LocalDate.parse(date,formatter);
            } else {
                localDate = LocalDate.now();
            }
            defaultExpenses = transactionService.getMonthTransactions(user, localDate);
        } catch (NullPointerException e) {
            log.warn("Something went wrong in the expens lists date controller");
        }
        modelAndView.addObject("transactions", defaultExpenses);
        modelAndView.addObject("datesFilters", transactionService.findMonthListWithTransactions(user));
        modelAndView.setViewName("transaction/list");
        return modelAndView;
    }

    // ==== ADD/EDIT TRANSACTIONS ====
    @RequestMapping(value="transaction/view", method = RequestMethod.GET)
    public ModelAndView addEditTransaction (@RequestParam(value="transactionID",required = false, defaultValue = "-1") int transactionId) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        DefaultExpense defaultExpense = transactionService.getTransaction(transactionId);
        if (!user.getDefaultExpenses().contains(defaultExpense)) {
            defaultExpense = new DefaultExpense();
        }
        modelAndView.addObject("transaction", defaultExpense);
        modelAndView.addObject("transactionCategoryList", user.getTransactionType());
        modelAndView.setViewName("transaction/view");
        return modelAndView;
    }
    @RequestMapping(value = "transaction/view", method = RequestMethod.POST)
    public ModelAndView processTransaction(@ModelAttribute("transaction") DefaultExpense defaultExpense) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        defaultExpense.setUser(user);
        transactionService.addTransaction(defaultExpense);
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    // ==== ARCHIVE TRANSACTIONS ====
    @RequestMapping(value="/transaction/archive", method=RequestMethod.GET)
    public ModelAndView archiveTransaction (@RequestParam(value="transactionID") int transactionID) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        DefaultExpense defaultExpense = transactionService.getTransaction(transactionID);
        if (user.getDefaultExpenses().contains(defaultExpense)) {
            transactionService.archiveTransaction(defaultExpense);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }

    // ==== REMOVE TRANSACTIONS ====
    @RequestMapping(value="/transaction/remove", method=RequestMethod.GET)
    public ModelAndView removeTransaction (@RequestParam(value="transactionID") int transactionID) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        DefaultExpense defaultExpense = transactionService.getTransaction(transactionID);
        if (user.getDefaultExpenses().contains(defaultExpense)) {
            transactionService.removeTransaction(defaultExpense);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }
}
