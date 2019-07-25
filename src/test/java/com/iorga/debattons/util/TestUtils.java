package com.iorga.debattons.util;

import com.iorga.debattons.domain.User;
import com.iorga.debattons.repository.UserRepository;

public class TestUtils {
    public static long USER_ID = 4L;

    public static User getUser(UserRepository userRepository) {
        return userRepository.getOne(USER_ID);
    }
}
