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
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // == public methods ==
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setRecoveryMode(0);
        user.setEmail(bCryptPasswordEncoder.encode(user.getEmail()));
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

    // This method is used to match the encrypted email with a raw email
    // TODO improvements Priority 3: Understand if this is safe
    public String verifyEncryptedEmail (String email, Boolean isRecoveryTime) {
        List<User> userList = userRepository.findByActive(1);
        for (User u : userList) {
            if (bCryptPasswordEncoder.matches(email, u.getEmail())) {
                if (isRecoveryTime) {
                    u.setRecoveryMode(1);
                    saveUser(u);
                }
                return u.getEmail();
            } else {
                return null;
            }
        }
        return null;
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

    public void recoverCredentials (String email) {
        emailService.sendSimpleMessage(email,
                "Recover your credentials on Piggus",
                "https://piggus.com/set-credentials?email=" + email + " to recover your credentials");
    }

    public void updateCredentials (String email, String password) {
        User user = findUserByEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRecoveryMode(0);
        saveUser(user);
    }
}
