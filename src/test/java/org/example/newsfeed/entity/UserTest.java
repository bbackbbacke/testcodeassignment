package org.example.newsfeed.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserBuilder() {
        UserStatusEnum status = UserStatusEnum.ACTIVE;

        User user = User.builder()
            .userId("userId")
            .password("password")
            .name("name")
            .email("email")
            .comment("comment")
            .refreshToken("refreshToken")
            .statusChangeTime(LocalDateTime.now().toString())
            .status(status)
            .build();

        assertEquals(UserStatusEnum.ACTIVE, user.getStatus());
    }
}
