/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * Controller to manage web requests related to the user
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

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
    @RequestMapping(value="/user/configuration", method = RequestMethod.GET)
    public ModelAndView userConfiguration () {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user",user);
        modelAndView.addObject("listSize", user.getExpenseType().size());
        modelAndView.setViewName("user/configuration");
        return modelAndView;
    }
    @RequestMapping(value="/set-budget", method = RequestMethod.POST)
    public ModelAndView processBudget (@ModelAttribute("user") User updatedUser) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        user.setMonthlyBudget(updatedUser.getMonthlyBudget());
        user.setMonthlySaving(updatedUser.getMonthlySaving());
        userService.saveUser(user);
        modelAndView.setViewName("redirect:/user/configuration");
        return modelAndView;
    }

    @RequestMapping(value="/set-expenses-type", method = RequestMethod.POST)
    public ModelAndView processExpensesType (@ModelAttribute("user") User updatedUser) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        user.setExpenseType(updatedUser.getExpenseType());
        userService.saveUser(user);
        modelAndView.setViewName("redirect:/user/configuration");
        return modelAndView;
    }
}
