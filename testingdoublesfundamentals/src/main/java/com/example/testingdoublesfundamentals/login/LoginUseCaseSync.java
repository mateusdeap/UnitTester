package com.example.testingdoublesfundamentals.login;

import com.example.testingdoublesfundamentals.login.data.AuthTokenCache;
import com.example.testingdoublesfundamentals.login.eventbus.EventBusPoster;
import com.example.testingdoublesfundamentals.login.network.LoggedInEvent;
import com.example.testingdoublesfundamentals.login.network.LoginHttpEndpointSync;
import com.example.testingdoublesfundamentals.login.network.NetworkErrorException;

public class LoginUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final LoginHttpEndpointSync loginHttpEndpointSync;
    private final AuthTokenCache authTokenCache;
    private final EventBusPoster eventBusPoster;

    public LoginUseCaseSync(LoginHttpEndpointSync loginHttpEndpointSync, AuthTokenCache authTokenCache, EventBusPoster eventBusPoster) {
        this.loginHttpEndpointSync = loginHttpEndpointSync;
        this.authTokenCache = authTokenCache;
        this.eventBusPoster = eventBusPoster;
    }

    public UseCaseResult loginSync(String username, String password) {
        try {
            LoginHttpEndpointSync.EndpointResult result = loginHttpEndpointSync.loginSync(username, password);
            if (endpointReturnsSuccess(result)) {
                authTokenCache.cacheAuthToken(result.getAuthToken());
                eventBusPoster.postEvent(new LoggedInEvent());
                return UseCaseResult.SUCCESS;
            } else {
                return UseCaseResult.FAILURE;
            }
        } catch (NetworkErrorException exception) {
            return UseCaseResult.NETWORK_ERROR;
        }
    }

    private boolean endpointReturnsSuccess(LoginHttpEndpointSync.EndpointResult result) {
        return result.getStatus() == LoginHttpEndpointSync.EndpointResultStatus.SUCCESS;
    }
}