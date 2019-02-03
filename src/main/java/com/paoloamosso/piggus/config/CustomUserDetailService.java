package com.paoloamosso.piggus.config;

import com.paoloamosso.piggus.dao.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TextEncryptor textEncryptor;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<com.paoloamosso.piggus.model.User> users = userRepository.findByActive(1);
        User user = null;
        for (com.paoloamosso.piggus.model.User u : users) {
            if (textEncryptor.decrypt(u.getEmail()).equals(email)) {
                user = buildUserFromUserEntity(u);
            }
        }
        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }

    private User buildUserFromUserEntity(com.paoloamosso.piggus.model.User userEntity) {
        // convert model user to spring security user
        String username = userEntity.getEmail();
        String password = userEntity.getPassword();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        User springUser = new User(username, password, enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked,
                authorities);
        return springUser;
    }
}
