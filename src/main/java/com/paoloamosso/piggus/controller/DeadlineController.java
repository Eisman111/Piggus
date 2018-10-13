/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * The deadline controller is necessary to manage the web requests
 */

package com.paoloamosso.piggus.controller;

import com.paoloamosso.piggus.model.Deadline;
import com.paoloamosso.piggus.model.User;
import com.paoloamosso.piggus.service.DeadlineService;
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

@Slf4j
@Controller
public class DeadlineController {

    // == fields ==
    private UserService userService;
    private DeadlineService deadlineService;

    // == constructor ==
    @Autowired
    public DeadlineController(UserService userService, DeadlineService deadlineService) {
        this.userService = userService;
        this.deadlineService = deadlineService;
    }

    // == handler methods ==
    // ==== VIEW DEADLINES ====
    @RequestMapping(value="/deadline/list", method = RequestMethod.GET)
    public ModelAndView listDeadlines () {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        modelAndView.addObject("deadlines", deadlineService.getDeadlinesList(user,false));
        modelAndView.addObject("archivedDeadlines", deadlineService.getDeadlinesList(user,true));
        modelAndView.setViewName("deadline/list");
        return modelAndView;
    }

    // ==== ADD/EDIT DEADLINES ====
    @RequestMapping(value="/deadline/view", method=RequestMethod.GET)
    public ModelAndView addEditDeadline (@RequestParam(value="deadlineID",required = false, defaultValue = "-1") int deadlineId) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Deadline deadline = deadlineService.getDeadline(deadlineId);
        if (!user.getDeadlines().contains(deadline)) {
            deadline = new Deadline();
        }
        modelAndView.addObject("deadline",deadline);
        modelAndView.setViewName("deadline/view");
        return modelAndView;
    }
    @RequestMapping(value="/update-deadline", method=RequestMethod.POST)
    public ModelAndView processDeadline(@ModelAttribute("deadline") Deadline deadline) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        deadline.setUser(user);
        deadlineService.saveDeadline(deadline);
        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    // ==== ARCHIVE DEADLINE ====
    @RequestMapping(value="/archive-deadline", method=RequestMethod.GET)
    public ModelAndView archiveDeadline (@RequestParam(value="deadlineID",required = false) int deadlineID) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Deadline deadline = deadlineService.getDeadline(deadlineID);
        if (user.getDeadlines().contains(deadline)) {
            deadline.setArchived(true);
            deadlineService.saveDeadline(deadline);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }

    // ==== REMOVE DEADLINE ====
    @RequestMapping(value="/remove-deadline", method=RequestMethod.GET)
    public ModelAndView removeDeadline (@RequestParam(value="deadlineID",required = false) int deadlineID) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Deadline deadline = deadlineService.getDeadline(deadlineID);
        if (user.getDeadlines().contains(deadline)) {
            deadlineService.removeDeadline(deadline);
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("redirect:/404");
        }
        return modelAndView;
    }
}
