package com.example.testdrivendevelopment;

import com.example.testdrivendevelopment.networking.FetchUserHttpEndpointSync;
import com.example.testdrivendevelopment.networking.NetworkErrorException;
import com.example.testdrivendevelopment.users.User;
import com.example.testdrivendevelopment.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.example.testdrivendevelopment.FetchUserUseCaseSync.*;
import static com.example.testdrivendevelopment.networking.FetchUserHttpEndpointSync.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncTest {

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "Mateus de Andrade Pereira";
    public static final User USER = new User(USER_ID, USER_NAME);

    private FetchUserUseCaseSync fetchUserUseCaseSync;

    @Mock
    private FetchUserHttpEndpointSync fetchUserHttpEndpointSyncMock;
    @Mock
    private UsersCache usersCacheMock;

    @Before
    public void setUp() throws Exception {
        fetchUserUseCaseSync =
                new FetchUserUseCaseSyncImpl(fetchUserHttpEndpointSyncMock, usersCacheMock);
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new EndpointResult(EndpointStatus.SUCCESS, USER_ID, USER_NAME));
    }

    @Test
    public void givenAnyParameters_fetchUser_passesThemToEndpoint() throws NetworkErrorException {
        // Arrange
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        // Act
        fetchUserUseCaseSync.fetchUserSync(USER_ID);
        // Assert
        verify(fetchUserHttpEndpointSyncMock, times(1))
                .fetchUserSync(argumentCaptor.capture());
        assertEquals(USER_ID, argumentCaptor.getValue());
    }

    @Test
    public void givenEndpointSucceeds_fetchUser_cachesUser() {
        // Arrange
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        // Act
        fetchUserUseCaseSync.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock, times(1))
                .cacheUser(argumentCaptor.capture());
        assertEquals(USER, argumentCaptor.getValue());
    }

    @Test
    public void givenEndpointSucceeds_fetchUser_returnsSuccess() {
        // Arrange
        // Act
        UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(USER_ID);
        // Assert
        assertEquals(Status.SUCCESS, result.getStatus());
    }

    @Test
    public void givenEndpointSucceeds_fetchUser_returnsANonNullUser() {
        // Arrange
        // Act
        UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(USER_ID);
        // Assert
        assertEquals(USER, result.getUser());
    }

    @Test
    public void givenEndpointFailsWithAuthError_fetchUser_returnsFailureAndDoesNotCacheUser() throws NetworkErrorException {
        // Arrange
        failWith(EndpointStatus.AUTH_ERROR);
        // Act
        UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock, times(0))
            .cacheUser(any(User.class));
        assertEquals(Status.FAILURE, result.getStatus());
    }

    @Test
    public void givenEndpointFailsWithGeneralError_fetchUser_returnsFailureAndDoesNotCacheUser() throws NetworkErrorException {
        // Arrange
        failWith(EndpointStatus.GENERAL_ERROR);
        // Act
        UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock, times(0))
                .cacheUser(any(User.class));
        assertEquals(Status.FAILURE, result.getStatus());
    }

    // If endpoint throws a NetworkErrorException - use case returns a network error

    @Test
    public void givenEndpointThrowsNetworkErrorException_fetchUser_returnsNetworkErrorAndDoesNotCacheUser() throws NetworkErrorException {
        // Arrange
        throwNetworkException();
        // Act
        UseCaseResult result = fetchUserUseCaseSync.fetchUserSync(USER_ID);
        // Assert
        verify(usersCacheMock, times(0))
                .cacheUser(any(User.class));
        assertEquals(Status.NETWORK_ERROR, result.getStatus());
    }

    private void failWith(EndpointStatus status) throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new EndpointResult(status, "", ""));
    }

    private void throwNetworkException() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenThrow(new NetworkErrorException());
    }
}