/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * The transaction controller is necessary to manage the web requests
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.model.Transaction;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.DeadlineService;
import com.paoloamosso.piggus.service.TransactionService;
import com.paoloamosso.piggus.service.UserService;
import lombok.extern.java.Log;
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
        List<Transaction> transactions = new ArrayList<>();
        LocalDate localDate;
        try {
            if (!date.equals("-1")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                localDate = LocalDate.parse(date,formatter);
            } else {
                localDate = LocalDate.now();
            }
            transactions = transactionService.getMonthTransactions(user, localDate);
        } catch (NullPointerException e) {
            log.warn("Something went wrong in the expens lists date controller");
        }
        modelAndView.addObject("transactions", transactions);
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
        Transaction transaction = transactionService.getTransaction(transactionId);
        if (!user.getTransactions().contains(transaction)) {
            transaction = new Transaction();
        }
        modelAndView.addObject("transaction", transaction);
        modelAndView.addObject("transactionCategoryList", user.getTransactionType());
        modelAndView.setViewName("transaction/view");
        return modelAndView;
    }
    @RequestMapping(value = "transaction/view", method = RequestMethod.POST)
    public ModelAndView processTransaction(@ModelAttribute("transaction") Transaction transaction) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        transaction.setUser(user);
        transactionService.addTransaction(transaction);
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    // ==== ARCHIVE TRANSACTIONS ====
    @RequestMapping(value="/transaction/archive", method=RequestMethod.GET)
    public ModelAndView archiveTransaction (@RequestParam(value="transactionID") int transactionID) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        Transaction transaction = transactionService.getTransaction(transactionID);
        if (user.getTransactions().contains(transaction)) {
            transactionService.archiveTransaction(transaction);
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
        Transaction transaction = transactionService.getTransaction(transactionID);
        if (user.getTransactions().contains(transaction)) {
            transactionService.removeTransaction(transaction);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }
}
