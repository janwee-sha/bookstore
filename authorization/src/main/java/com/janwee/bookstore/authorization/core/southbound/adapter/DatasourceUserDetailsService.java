package com.janwee.bookstore.authorization.core.southbound.adapter;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatasourceUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Autowired
    public DatasourceUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.userOfEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " is not found"));
        return SpringSecurityUserRepositoryJpaAdapter.toSpringSecurityUser(user);
    }
}
