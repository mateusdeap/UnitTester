package com.example.mockitofundamentals;


import com.example.mockitofundamentals.eventbus.EventBusPoster;
import com.example.mockitofundamentals.eventbus.UserDetailsChangedEvent;
import com.example.mockitofundamentals.networking.NetworkErrorException;
import com.example.mockitofundamentals.networking.UpdateUsernameHttpEndpointSync;
import com.example.mockitofundamentals.user.User;
import com.example.mockitofundamentals.user.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.example.mockitofundamentals.UpdateUsernameUseCaseSync.UseCaseResult;
import static com.example.mockitofundamentals.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import static com.example.mockitofundamentals.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    public static final String USER_ID = "123456";
    public static final String USERNAME = "Mateus";
    public static final User USER = new User(USER_ID, USERNAME);

    private UpdateUsernameUseCaseSync updateUsernameUseCaseSync;

    @Mock
    private UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;
    @Mock
    private UsersCache usersCacheMock;
    @Mock
    private EventBusPoster eventBusPosterMock;

    @Before
    public void setUp() throws Exception {
        updateUsernameUseCaseSync = new UpdateUsernameUseCaseSync(
                updateUsernameHttpEndpointSyncMock,
                usersCacheMock,
                eventBusPosterMock
        );

        when(updateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
            .thenReturn(new EndpointResult(EndpointResultStatus.SUCCESS, USER_ID, USERNAME));
    }

    @Test
    public void givenUpdateUserNameWasCalledWithUserIdAndUserName_updateUserNameSync_callsUpdateUsernameHttpEndpoint() throws Exception {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verify(updateUsernameHttpEndpointSyncMock, times(1))
                .updateUsername(argumentCaptor.capture(), argumentCaptor.capture());
        List<String> captures = argumentCaptor.getAllValues();
        assertEquals(USER_ID, captures.get(0));
        assertEquals(USERNAME, captures.get(1));
    }

    // given update username http endpoint succeeds, the user is cached
    @Test
    public void givenUpdateUserNameHttpEndpointSucceeds_updateUserNameSync_cachesUser() throws Exception {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verify(usersCacheMock).cacheUser(captor.capture());
        assertEquals(USER, captor.getValue());
    }

    // given update username http endpoint succeeds, the user details changed event is posted
    @Test
    public void givenUpdateUserNameHttpEndpointSucceeds_updateUserNameSync_postsUserDetailsChangedEvent() throws Exception {
        ArgumentCaptor<UserDetailsChangedEvent> captor = ArgumentCaptor.forClass(UserDetailsChangedEvent.class);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verify(eventBusPosterMock).postEvent(captor.capture());
        assertEquals(UserDetailsChangedEvent.class, captor.getValue().getClass());
    }

    // given update username http endpoint succeeds, update user returns SUCCESS
    @Test
    public void givenUpdateUserNameHttpEndpointSucceeds_updateUserNameSync_returnsSuccess() throws Exception {
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.SUCCESS, result);
    }

    // given update username http endpoint fails, no user is cached
    @Test
    public void givenUpdateUserNameHttpEndpointFails_updateUserNameSync_doesNotCacheAnyUsers() throws Exception {
        // Fails with GENERAL_ERROR
        failsWith(EndpointResultStatus.GENERAL_ERROR);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);

        // Fails with SERVER_ERROR
        failsWith(EndpointResultStatus.SERVER_ERROR);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);

        // Fails with AUTH_ERROR
        failsWith(EndpointResultStatus.AUTH_ERROR);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    // given update username http endpoint fails, no user details changed event is posted
    @Test
    public void givenUpdateUserNameHttpEndpointFails_updateUserNameSync_doesNotPostAUserDetailsChangedEvent() throws Exception {
        // Fails with GENERAL_ERROR
        failsWith(EndpointResultStatus.GENERAL_ERROR);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);

        // Fails with SERVER_ERROR
        failsWith(EndpointResultStatus.SERVER_ERROR);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);

        // Fails with AUTH_ERROR
        failsWith(EndpointResultStatus.AUTH_ERROR);
        updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    // given update username http endpoint fails with auth error, update user returns a FAILURE
    @Test
    public void givenUpdateUserNameHttpEndpointFailsWithAuthError_updateUserNameSync_returnsFailure() throws Exception {
        failsWith(EndpointResultStatus.AUTH_ERROR);
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    // given update username http endpoint fails with server error, update user returns a NETWORK_ERROR
    @Test
    public void givenUpdateUserNameHttpEndpointFailsWithServerError_updateUserNameSync_returnsNetworkError() throws Exception {
        failsWith(EndpointResultStatus.SERVER_ERROR);
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }

    // given update username http endpoint fails with general error, update user returns a FAILURE
    @Test
    public void givenUpdateUserNameHttpEndpointFailsWithServerError_updateUserNameSync_returnsFailure() throws Exception {
        failsWith(EndpointResultStatus.GENERAL_ERROR);
        UseCaseResult result = updateUsernameUseCaseSync.updateUsernameSync(USER_ID, USERNAME);
        assertEquals(UseCaseResult.FAILURE, result);
    }

    private void failsWith(EndpointResultStatus status) throws NetworkErrorException {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(anyString(), anyString()))
                .thenReturn(new EndpointResult(status, "", ""));
    }
}
