package com.example.testdrivendevelopment;

import com.example.testdrivendevelopment.networking.GetReputationHttpEndpointSync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.example.testdrivendevelopment.GetReputationUseCaseSync.Status;
import static com.example.testdrivendevelopment.GetReputationUseCaseSync.UseCaseResult;
import static com.example.testdrivendevelopment.networking.GetReputationHttpEndpointSync.EndpointResult;
import static com.example.testdrivendevelopment.networking.GetReputationHttpEndpointSync.EndpointStatus;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetReputationUseCaseSyncTest {

    public static final int REPUTATION = 35;
    private GetReputationUseCaseSync getReputationUseCaseSync;

    @Mock
    private GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;

    @Before
    public void setUp() {
        getReputationUseCaseSync =
                new GetReputationUseCaseSyncImpl(getReputationHttpEndpointSyncMock);
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(EndpointStatus.SUCCESS, REPUTATION));
    }

    @Test
    public void givenGetReputationIsCalled_getReputation_callHttpEndpoint() {
        // Arrange
        // Act
        getReputationUseCaseSync.getReputation();
        // Assert
        verify(getReputationHttpEndpointSyncMock, times(1))
                .getReputationSync();
    }

    @Test
    public void givenEndpointReturnsSuccess_getReputation_returnsSuccess() {
        // Arrange
        // Act
        UseCaseResult result = getReputationUseCaseSync.getReputation();
        // Assert
        assertEquals(Status.SUCCESS, result.getStatus());
    }

    @Test
    public void givenEndpointSuccess_getReputation_returnsReputationScore() {
        // Arrange
        // Act
        UseCaseResult result = getReputationUseCaseSync.getReputation();
        // Assert
        assertTrue(result.getReputation() >= 0);
    }

    @Test
    public void givenEndpointFailsWithGeneralError_getReputation_returnsFailure() {
        // Arrange
        failWith(EndpointStatus.GENERAL_ERROR);
        // Act
        UseCaseResult result = getReputationUseCaseSync.getReputation();
        // Assert
        assertEquals(Status.FAILURE, result.getStatus());
    }

    @Test
    public void givenEndpointFailsWithNetworkError_getReputation_returnsFailure() {
        // Arrange
        failWith(EndpointStatus.NETWORK_ERROR);
        // Act
        UseCaseResult result = getReputationUseCaseSync.getReputation();
        // Assert
        assertEquals(Status.FAILURE, result.getStatus());
    }

    private void failWith(EndpointStatus status) {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(status, 0));
    }
}