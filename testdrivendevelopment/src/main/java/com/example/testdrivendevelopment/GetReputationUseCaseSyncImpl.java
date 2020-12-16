package com.example.testdrivendevelopment;

import com.example.testdrivendevelopment.networking.GetReputationHttpEndpointSync;

import org.jetbrains.annotations.NotNull;

import static com.example.testdrivendevelopment.networking.GetReputationHttpEndpointSync.EndpointResult;
import static com.example.testdrivendevelopment.networking.GetReputationHttpEndpointSync.EndpointStatus;

public class GetReputationUseCaseSyncImpl implements GetReputationUseCaseSync {
    private final GetReputationHttpEndpointSync httpEndpoint;

    public GetReputationUseCaseSyncImpl(GetReputationHttpEndpointSync httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }

    @Override
    public @NotNull UseCaseResult getReputation() {
        EndpointResult result = httpEndpoint.getReputationSync();

        if (result.getStatus() == EndpointStatus.SUCCESS)
            return new UseCaseResult(Status.SUCCESS, result.getReputation());

        return new UseCaseResult(Status.FAILURE, 0);
    }
}
