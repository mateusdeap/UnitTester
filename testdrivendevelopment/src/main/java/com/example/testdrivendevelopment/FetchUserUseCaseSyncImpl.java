package com.example.testdrivendevelopment;

import com.example.testdrivendevelopment.networking.FetchUserHttpEndpointSync;
import com.example.testdrivendevelopment.networking.NetworkErrorException;
import com.example.testdrivendevelopment.users.User;
import com.example.testdrivendevelopment.users.UsersCache;

public class FetchUserUseCaseSyncImpl implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSync fetchUserHttpEndpointSync;
    private UsersCache usersCache;

    public FetchUserUseCaseSyncImpl(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        this.fetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.usersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        FetchUserHttpEndpointSync.EndpointResult result;
        User user;

        if ((user = usersCache.getUser(userId)) != null) {
            return new UseCaseResult(Status.SUCCESS, user);
        }

        try {
            result = fetchUserHttpEndpointSync.fetchUserSync(userId);

            if (result.getStatus() != FetchUserHttpEndpointSync.EndpointStatus.SUCCESS)
                    return new UseCaseResult(Status.FAILURE, null);
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }

        user = new User(result.getUserId(), result.getUsername());
        usersCache.cacheUser(user);
        return new UseCaseResult(Status.SUCCESS, user);
    }
}
