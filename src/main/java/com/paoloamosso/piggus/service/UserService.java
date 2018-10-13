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

import java.time.LocalDate;
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

    public User findUserByUniqueID(String uniqueID) {
        return userRepository.findByUserPublicIdentifier(uniqueID);
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
        user.setRegistrationDate(LocalDate.now());

        // Adding default expenses types
        Set<String> expenseType = new HashSet<>();
        expenseType.add("Groceries");
        expenseType.add("Transportation");
        expenseType.add("Entertainment");
        user.setExpenseType(expenseType);

        user.setMonthlyBudget(0.0);
        user.setMonthlySaving(0.0);

        // ==== MANAGING UNIQUE IDENTIFIERS FOR USERS ====
        // We generate a unique code for each user so that we can use it later on
        // We check if the code is unique
        // TODO refactor Priority 2: Find a better way to manage this
        // ====== PUBLIC IDENTIFIER ======
        Boolean isUnique;
        do {
            isUnique = true;
            String userPublicIdentifier = UUID.randomUUID().toString();
            for (User u : userRepository.findByActive(1)) {
                if (userPublicIdentifier.equals(u.getUserPublicIdentifier())) {
                    isUnique = false;
                    break;
                }
            }
            if (isUnique) {
                user.setUserPublicIdentifier(userPublicIdentifier);
            }
        } while (!isUnique);
        // ====== PRIVATE IDENTIFIER ======
        do {
            isUnique = true;
            String userSecretIdentifier = UUID.randomUUID().toString();
            for (User u : userRepository.findByActive(1)) {
                if (userSecretIdentifier.equals(u.getUserSecretIdentifier())) {
                    isUnique = false;
                    break;
                }
            }
            if (isUnique) {
                user.setUserSecretIdentifier(userSecretIdentifier);
            }
        } while (!isUnique);

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
                return u.getUserPublicIdentifier();
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

    public void recoverCredentials (String email, String uniqueID) {
        emailService.sendSimpleMessage(email,
                "Recover your credentials on Piggus",
                "Click on the link to recover your credentials: https://piggus.com/set-credentials?id=" + uniqueID);
    }

    public void updateCredentials (String email, String password) {
        User user = findUserByEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRecoveryMode(0);
        saveUser(user);
    }
}
