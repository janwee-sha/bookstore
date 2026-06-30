package com.janwee.bookstore.authorization.northbound.local;

import com.janwee.bookstore.authorization.domain.UserNotFoundException;
import com.janwee.bookstore.authorization.domain.UserRegistrationService;
import com.janwee.bookstore.authorization.northbound.message.SigningUpRequest;
import com.janwee.bookstore.authorization.northbound.message.UserResponse;
import com.janwee.bookstore.authorization.southbound.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserRegistrationService userRegistrationService;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public List<UserResponse> users() {
        return userRepo.users().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse userOf(String username) {
        return userRepo.userOfEmail(username)
                .map(UserResponse::from)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void signup(SigningUpRequest request) {
        userRegistrationService.add(request.toUser());
    }
}
