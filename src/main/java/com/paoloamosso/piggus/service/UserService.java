/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * This is the bridge between the repository and the controller
 */

package com.paoloamosso.piggus.service;

import com.paoloamosso.piggus.dao.RoleRepository;
import com.paoloamosso.piggus.dao.UserRepository;
import com.paoloamosso.piggus.model.Deadline;
import com.paoloamosso.piggus.model.Expense;
import com.paoloamosso.piggus.model.Role;
import com.paoloamosso.piggus.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userService")
public class UserService {

    // == fields ==
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    @Autowired
    @Qualifier("roleRepository")
    private RoleRepository roleRepository;
    @Autowired
    @Qualifier("expenseService")
    private ExpenseService expenseService;
    @Autowired
    @Qualifier("deadlineService")
    private DeadlineService deadlineService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // == public methods ==
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setDeadlines(new ArrayList<>());
        user.setExpenses(new ArrayList<>());

        // Adding default expenses types
        Set<String> expenseType = new HashSet<>();
        expenseType.add("Groceries");
        expenseType.add("Transportation");
        expenseType.add("Entertainment");
        user.setExpenseType(expenseType);

        user.setMonthlyBudget(0.0);
        user.setMonthlySaving(0.0);
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<Expense> getExpenses (User user) {
        return expenseService.getExpenses(user);
    }

    public List<Deadline> getDeadlines (User user) {
        return deadlineService.getDeadlinesList(user);
    }
}
