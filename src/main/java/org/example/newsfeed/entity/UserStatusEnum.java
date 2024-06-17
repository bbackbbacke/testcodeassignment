package org.example.newsfeed.entity;

import lombok.Getter;

@Getter
public enum UserStatusEnum {
    ACTIVE("ACTIVE", "정상 회원입니다."),
    WITHDRAWN("WITHDRAWN", "탈퇴한 회원입니다.");

    private final String value;
    private final String message;

    UserStatusEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
