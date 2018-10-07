/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

// This class is necessary for the security functionality, here we define the handlers for security

/*
 * This controller is necessary to manage the security requests
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.RoleService;
import com.paoloamosso.piggus.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Slf4j
@Controller
public class LoginController {

    // == fields ==
    private UserService userService;
    private RoleService roleService;

    // == constructor ==
    @Autowired
    public LoginController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // == init ==
    @PostConstruct
    public void createUser () {
        log.info("started the creation of the user");
        roleService.createDefaultRole();
    }

    // == handlers requests ==
    // ==== LOGIN ====
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    // ==== REGISTER ====
    @RequestMapping(value= "/register", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUsername(user.getUsername());
        if (userExists != null) {
            modelAndView.setViewName("register");
            modelAndView.addObject("successMessage", "The username already exists");
        } else {
            if (userService.verifyEncryptedEmail(user.getEmail(),false)!=null) {
                modelAndView.setViewName("register");
                modelAndView.addObject("successMessage", "The email already exists");
            } else {
                userService.createUser(user);
                modelAndView.setViewName("redirect:/login");
            }
        }
        return modelAndView;
    }

    // ==== RECOVER CREDENTIALS ====
    @RequestMapping(value = "/recover-credentials", method = RequestMethod.GET)
    public ModelAndView recoverCredentials(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("recover-credentials");
        return modelAndView;
    }

    @RequestMapping(value = "/recover-credentials", method = RequestMethod.POST)
    public ModelAndView processRecoverCredentials(@ModelAttribute("email") String email) {
        ModelAndView modelAndView = new ModelAndView();
        String hashedEmail = userService.verifyEncryptedEmail(email,true);
        if (hashedEmail != null) {
            modelAndView.setViewName("recover-credentials");
            userService.recoverCredentials(email);
            modelAndView.addObject("successMessage", "We have sent you an email with the instructions");
        } else {
            modelAndView.setViewName("recover-credentials");
            modelAndView.addObject("successMessage", "The email does not exist");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/set-credentials", method = RequestMethod.GET)
    public ModelAndView setCredentials(@RequestParam(value = "email", required = true) String email) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByEmail(userService.verifyEncryptedEmail(email,false));
        if (user.getRecoveryMode() == 1) {
            modelAndView.setViewName("set-credentials");
            modelAndView.addObject("username", user.getUsername());
            modelAndView.addObject("email", user.getEmail());
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/set-credentials", method = RequestMethod.POST)
    public ModelAndView processSetCredentials (@ModelAttribute("email") String email, @ModelAttribute("password") String password) {
        ModelAndView modelAndView = new ModelAndView();
        userService.updateCredentials(email,password);
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }
}
