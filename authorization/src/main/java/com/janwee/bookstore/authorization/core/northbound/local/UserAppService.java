package com.janwee.bookstore.authorization.core.northbound.local;

import com.janwee.bookstore.authorization.core.domain.UserService;
import com.janwee.bookstore.authorization.core.northbound.message.SigningUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAppService {
    private final UserService userService;

    @Transactional(rollbackFor = Throwable.class)
    public void signup(SigningUpRequest request) {
        userService.add(request.toUser());
    }
}
