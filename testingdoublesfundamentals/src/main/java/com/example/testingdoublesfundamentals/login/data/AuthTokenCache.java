package com.example.testingdoublesfundamentals.login.data;

public interface AuthTokenCache {

    void cacheAuthToken(String authToken);
    String getAuthToken();
}
