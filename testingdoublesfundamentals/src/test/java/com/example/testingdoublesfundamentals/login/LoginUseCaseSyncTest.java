package com.example.testingdoublesfundamentals.login;

import com.example.testingdoublesfundamentals.login.data.AuthTokenCache;
import com.example.testingdoublesfundamentals.login.eventbus.EventBusPoster;
import com.example.testingdoublesfundamentals.login.network.LoggedInEvent;
import com.example.testingdoublesfundamentals.login.network.LoginHttpEndpointSync;
import com.example.testingdoublesfundamentals.login.LoginUseCaseSync;
import com.example.testingdoublesfundamentals.login.network.NetworkErrorException;

import org.junit.Before;
import org.junit.Test;

import static com.example.testingdoublesfundamentals.login.LoginUseCaseSync.*;
import static org.junit.Assert.assertEquals;

public class LoginUseCaseSyncTest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String AUTH_TOKEN = "authToken";

    private LoginHttpEndpointSyncTd loginHttpEndpointSyncTd;
    private AuthTokenCacheTd authTokenCacheTd;
    private EventBusPosterTd eventBusPosterTd;

    private LoginUseCaseSync loginUseCaseSync;

    @Before
    public void setup() {
        loginHttpEndpointSyncTd = new LoginHttpEndpointSyncTd();
        authTokenCacheTd = new AuthTokenCacheTd();
        eventBusPosterTd = new EventBusPosterTd();
        loginUseCaseSync = new LoginUseCaseSync(
                loginHttpEndpointSyncTd,
                authTokenCacheTd,
                eventBusPosterTd
        );
    }

    @Test
    public void givenLoginSyncWasCalled_loginSync_passesUsernameAndPasswordToLoginHttpEndpointSync() {
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(USERNAME, loginHttpEndpointSyncTd.username);
        assertEquals(PASSWORD, loginHttpEndpointSyncTd.password);
    }

    @Test
    public void givenLoginSucceeds_loginSync_cachesAuthToken() {
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(AUTH_TOKEN, authTokenCacheTd.authToken);
    }

    @Test
    public void givenLoginFailsWithGeneralError_loginSync_doesNotChageAuthToken() {
        loginHttpEndpointSyncTd.returnsGeneralError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals("", authTokenCacheTd.authToken);
    }

    @Test
    public void givenLoginFailsWithAuthError_loginSync_doesNotChageAuthToken() {
        loginHttpEndpointSyncTd.returnsAuthError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals("", authTokenCacheTd.authToken);
    }

    @Test
    public void givenLoginFailsWithServerError_loginSync_doesNotChageAuthToken() {
        loginHttpEndpointSyncTd.returnsServerError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals("", authTokenCacheTd.authToken);
    }

    @Test
    public void givenLoginSucceeds_loginSync_postsLoginEventToEventBus() {
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(eventBusPosterTd.event.getClass(), LoggedInEvent.class);
    }

    @Test
    public void givenLoginFailsWithGeneralError_loginSync_noLoginEventPosted() {
        loginHttpEndpointSyncTd.returnsGeneralError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(0, eventBusPosterTd.interactionsCount);
    }

    @Test
    public void givenLoginFailsWithAuthError_loginSync_noLoginEventPosted() {
        loginHttpEndpointSyncTd.returnsAuthError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(0, eventBusPosterTd.interactionsCount);
    }

    @Test
    public void givenLoginFailsWithServerError_loginSync_noLoginEventPosted() {
        loginHttpEndpointSyncTd.returnsServerError = true;
        loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(0, eventBusPosterTd.interactionsCount);
    }

    @Test
    public void givenLoginSucceeds_loginSync_returnsSuccess() {
        UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(UseCaseResult.SUCCESS, result);
    }

    @Test
    public void givenLoginFailsWithGeneralError_loginSync_returnsFailure() {
        loginHttpEndpointSyncTd.returnsGeneralError = true;
        UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(UseCaseResult.FAILURE, result);
    }

    @Test
    public void givenLoginFailsWithAuthError_loginSync_returnsFailure() {
        loginHttpEndpointSyncTd.returnsAuthError = true;
        UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(UseCaseResult.FAILURE, result);
    }

    @Test
    public void givenLoginFailsWithServerError_loginSync_returnsFailure() {
        loginHttpEndpointSyncTd.returnsServerError = true;
        UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(UseCaseResult.FAILURE, result);
    }

    @Test
    public void givenLoginFailsWithNetworkError_loginSync_returnsNetworkError() {
        loginHttpEndpointSyncTd.returnsNetworkError = true;
        UseCaseResult result = loginUseCaseSync.loginSync(USERNAME, PASSWORD);

        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }


    //----------------------------------------------------------------------------------------------
    //------------------------------------- HELPER CLASSES -----------------------------------------
    //----------------------------------------------------------------------------------------------

    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {
        public String username = "";
        public String password = "";
        public boolean returnsGeneralError;
        public boolean returnsAuthError;
        public boolean returnsServerError;
        public boolean returnsNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            this.username = username;
            this.password = password;

            if (returnsGeneralError)
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");

            if (returnsAuthError)
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");

            if (returnsServerError)
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");

            if (returnsNetworkError)
                throw new NetworkErrorException();

            return new EndpointResult(EndpointResultStatus.SUCCESS, AUTH_TOKEN);
        }
    }

    private static class AuthTokenCacheTd implements AuthTokenCache {
        String authToken = "";

        @Override
        public void cacheAuthToken(String authToken) {
            this.authToken = authToken;
        }

        @Override
        public String getAuthToken() {
            return authToken;
        }
    }

    private static class EventBusPosterTd implements EventBusPoster {
        public Object event;
        public int interactionsCount;

        @Override
        public void postEvent(Object event) {
            this.event = event;
            interactionsCount++;
        }
    }
}