/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * The transaction controller is necessary to manage the web requests
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.model.transaction.*;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.model.transportation.DefaultTransactionRequest;
import com.paoloamosso.piggus.model.transportation.DefaultTransactionRequestBuilder;
import com.paoloamosso.piggus.service.DeadlineService;
import com.paoloamosso.piggus.service.DefaultTransactionService;
import com.paoloamosso.piggus.service.UserService;
import com.paoloamosso.piggus.util.EnumUtils;
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
    private DefaultTransactionService defaultTransactionService;
    private UserService userService;
    private TransactionBuilder<DefaultTransactionTypes> transactionBuilder = new DefaultTransactionBuilder();
    EnumUtils<DefaultTransactionTypes> enumUtilsDefaultTransactions = new EnumUtils(DefaultTransactionTypes.class);

    // == constructor ==
    @Autowired
    public TransactionController(DefaultTransactionService expensesService, UserService userService, DeadlineService deadlineService) {
        this.defaultTransactionService = expensesService;
        this.userService = userService;
    }

    // == handler methods ==
    // ==== VIEW TRANSACTIONS ====
    @RequestMapping(value="/transaction/list", method = RequestMethod.GET)
    public ModelAndView listTransactions (@RequestParam(value="date",required = false, defaultValue = "-1") String date) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        List<Transaction> defaultExpenses = new ArrayList<>();
        LocalDate localDate;
        try {
            if (!date.equals("-1")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                localDate = LocalDate.parse(date,formatter);
            } else {
                localDate = LocalDate.now();
            }
//            defaultExpenses = defaultTransactionService.getMonthTransactions(user, localDate);
        } catch (NullPointerException e) {
            log.warn("Something went wrong in the expens lists date controller");
        }
        modelAndView.addObject("transactions", defaultExpenses);
        modelAndView.addObject("datesFilters", defaultTransactionService.findMonthListWithTransactions(user));
        modelAndView.setViewName("transaction/list");
        return modelAndView;
    }

    // ==== ADD/EDIT TRANSACTIONS ====
    @RequestMapping(value="transaction/view", method = RequestMethod.GET)
    public ModelAndView addEditTransaction (@RequestParam(value="transactionID",required = false, defaultValue = "-1") int transactionId) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        Transaction transaction = defaultTransactionService.getTransaction(transactionId);
        DefaultTransactionRequest request;
        if (!user.getTransactions().contains(transaction)) {
            request = new DefaultTransactionRequest();
        } else {
            request = (DefaultTransactionRequest) new DefaultTransactionRequestBuilder<DefaultTransactionRequest>()
                    .start()
                    .convertTransaction(transaction)
                    .build();
        }

        modelAndView.addObject("defaultTransactionRequest", request);
        modelAndView.addObject("transactionCategoryList", user.getCategory());
        modelAndView.setViewName("transaction/view");
        return modelAndView;
    }
    @RequestMapping(value = "transaction/view", method = RequestMethod.POST)
    public ModelAndView processTransaction(@ModelAttribute DefaultTransactionRequest defaultTransactionRequest) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());

        Transaction transaction = transactionBuilder
                .start(enumUtilsDefaultTransactions.stringToEnum(defaultTransactionRequest.getTransactionType()))
                .convertTransportationRequest(defaultTransactionRequest)
                .build();
        transaction.setUser(user);
        defaultTransactionService.addTransaction(transaction);
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    // ==== ARCHIVE TRANSACTIONS ====
    @RequestMapping(value="/transaction/archive", method=RequestMethod.GET)
    public ModelAndView archiveTransaction (@RequestParam(value="transactionID") int transactionID) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEncryptedEmail(auth.getName());
        Transaction defaultExpense = defaultTransactionService.getTransaction(transactionID);
        if (user.getTransactions().contains(defaultExpense)) {
            defaultTransactionService.archiveTransaction(defaultExpense);
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
        Transaction defaultExpense = defaultTransactionService.getTransaction(transactionID);
        if (user.getTransactions().contains(defaultExpense)) {
            defaultTransactionService.removeTransaction(defaultExpense);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }
}
