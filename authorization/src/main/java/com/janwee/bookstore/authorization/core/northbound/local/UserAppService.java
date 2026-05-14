package com.janwee.bookstore.authorization.core.northbound.local;

import com.janwee.bookstore.authorization.core.domain.User;
import com.janwee.bookstore.authorization.core.domain.UserNotFoundException;
import com.janwee.bookstore.authorization.core.domain.UserService;
import com.janwee.bookstore.authorization.core.northbound.message.SigningUpRequest;
import com.janwee.bookstore.authorization.core.southbound.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserService userService;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public List<User> users() {
        return userRepo.users();
    }

    @Transactional(readOnly = true)
    public User userOf(String username) {
        return userRepo.userOfEmail(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void signup(SigningUpRequest request) {
        userService.add(request.toUser());
    }
}
