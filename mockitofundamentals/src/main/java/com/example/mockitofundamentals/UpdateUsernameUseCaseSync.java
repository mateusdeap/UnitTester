package com.example.mockitofundamentals;

import com.example.mockitofundamentals.eventbus.EventBusPoster;
import com.example.mockitofundamentals.eventbus.UserDetailsChangedEvent;
import com.example.mockitofundamentals.networking.NetworkErrorException;
import com.example.mockitofundamentals.networking.UpdateUsernameHttpEndpointSync;
import com.example.mockitofundamentals.user.User;
import com.example.mockitofundamentals.user.UsersCache;

import static com.example.mockitofundamentals.networking.UpdateUsernameHttpEndpointSync.EndpointResult;
import static com.example.mockitofundamentals.networking.UpdateUsernameHttpEndpointSync.EndpointResultStatus;

public class UpdateUsernameUseCaseSync {
    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSync;
    private final UsersCache usersCache;
    private final EventBusPoster eventBusPoster;

    public UpdateUsernameUseCaseSync(UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSync, UsersCache usersCache, EventBusPoster eventBusPoster) {
        this.updateUsernameHttpEndpointSync = updateUsernameHttpEndpointSync;
        this.usersCache = usersCache;
        this.eventBusPoster = eventBusPoster;
    }

    public UseCaseResult updateUsernameSync(String userId, String username) throws NetworkErrorException {
        EndpointResult result = updateUsernameHttpEndpointSync.updateUsername(userId, username);
        if (result.getStatus() == EndpointResultStatus.SUCCESS) {
            User user = new User(result.getUserId(), result.getUserName());
            usersCache.cacheUser(user);
            eventBusPoster.postEvent(new UserDetailsChangedEvent(user));
            return UseCaseResult.SUCCESS;
        } else if (result.getStatus() == EndpointResultStatus.SERVER_ERROR) {

        }
        return UseCaseResult.FAILURE;
    }
}