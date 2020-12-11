package com.example.mockitofundamentals.user;

public interface UsersCache {
    void cacheUser(User user);
    User getUser(String userId);
}
