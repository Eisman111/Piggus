/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Controller to manage web requests related to the user
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class UserController {

    // == fields ==
    UserService userService;

    // == constructor ==
    public UserController (UserService userService) {
        this.userService = userService;
    }

    // == handler methods ==
    // ==== CONFIGURING THE USER
//    @RequestMapping(value="/user/configuration", method = RequestMethod.GET)
//    public ModelAndView userConfiguration (@RequestParam(value = "successMessage", required = false) String successMessage) {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEncryptedEmail(auth.getName());
//        modelAndView.addObject("user",user);
//        modelAndView.addObject("listSize", user.getExpenseType().size());
//        if (successMessage != null) {
//            if (successMessage.equals("The budged has been saved")) {
//                modelAndView.addObject("successMessageBudget", "The budged has been saved");
//            } else {
//                modelAndView.addObject("successMessageExpenses", "The expens have been saved");
//            }
//        }
//        modelAndView.setViewName("user/configuration");
//        return modelAndView;
//    }

//    // TODO refactor Priority 2: Find a better way to manage this
//    @RequestMapping(value="/set-budget", method = RequestMethod.POST)
//    public ModelAndView processBudget (@ModelAttribute("user") User updatedUser) {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEncryptedEmail(auth.getName());
//        user.setMonthlyBudget(updatedUser.getMonthlyBudget());
//        user.setMonthlySaving(updatedUser.getMonthlySaving());
//        userService.saveUser(user);
//        modelAndView.setViewName("redirect:/user/configuration");
//        modelAndView.addObject("successMessage", "The budged has been saved");
//        return modelAndView;
//    }
//
//    @RequestMapping(value="/set-expenses-type", method = RequestMethod.POST)
//    public ModelAndView processExpensesType (@ModelAttribute("user") User updatedUser) {
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEncryptedEmail(auth.getName());
//        user.setExpenseType(updatedUser.getExpenseType());
//        userService.saveUser(user);
//        modelAndView.setViewName("redirect:/user/configuration");
//        modelAndView.addObject("successMessage", "The expens have been saved");
//        return modelAndView;
//    }

    // ==== EXPIRED SESSION CONTROL ====
    @RequestMapping(value = "/session-expired", method = RequestMethod.GET)
    public ModelAndView expiredSession () {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("session-expired");
        return modelAndView;
    }
}
