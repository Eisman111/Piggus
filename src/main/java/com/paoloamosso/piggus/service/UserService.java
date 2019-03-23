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
import com.paoloamosso.piggus.model.Role;
import com.paoloamosso.piggus.model.User;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("userService")
public class UserService {

    // == fields ==
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private DeadlineService deadlineService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TextEncryptor textEncryptor;

    // == public methods ==
    public User findUserByEncryptedEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByDecryptedEmail(String email) {
        List<User> users = userRepository.findByActive(1);
        for (User u : users) {
            String emailDecrypted = textEncryptor.decrypt(u.getEmail());
            if (emailDecrypted.equals(email)) {
                return u;
            }
        }
        return null;
    }

    public User findUserByPublicIdentifier(String uniqueID) {
        return userRepository.findByUserPublicIdentifier(uniqueID);
    }

    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setIsConfigured(0);
        user.setRecoveryMode(0);
        user.setEmail(textEncryptor.encrypt(user.getEmail()));
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setDeadlines(new ArrayList<>());
        user.setTransactions(new ArrayList<>());
//        user.setRegistrationDate(LocalDate.now());

        // Adding default expens types
        Set<String> expenseType = new HashSet<>();
        expenseType.add("Groceries");
        expenseType.add("Transportation");
        expenseType.add("Entertainment");
        user.setTransactionType(expenseType);

        user.setMonthlySaving(0.0);
        user.setTotalSavings(0.0);

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

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void recoverCredentials (String email, String uniqueID) {
        emailService.sendSimpleMessage(email,
                "Recover your credentials on Piggus",
                "Click on the link to recover your credentials: https://piggus.com/set-credentials?id=" + uniqueID);
    }

    public void updateCredentials (String email, String password) {
        User user = userRepository.findByEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRecoveryMode(0);
        saveUser(user);
    }
}
