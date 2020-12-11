package com.example.mockitofundamentals.eventbus;

import com.example.mockitofundamentals.user.User;

public class UserDetailsChangedEvent {

    private final User user;

    public UserDetailsChangedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
