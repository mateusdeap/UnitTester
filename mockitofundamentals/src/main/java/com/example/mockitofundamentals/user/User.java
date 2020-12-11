package com.example.mockitofundamentals.user;

public class User {
    private final String userId;
    private final String userName;

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || (getClass() != obj.getClass()))
            return false;

        User other = (User) obj;
        return other.userId.equals(this.userId) && other.userName.equals(this.userName);
    }
}
