package com.example.testingdoublesfundamentals.fetchuserprofile;

import static com.example.testingdoublesfundamentals.fetchuserprofile.UserProfileHttpEndpointSync.*;

public class FetchUserProfileUseCaseSync {

    public enum UseCaseResult {
        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    private final UserProfileHttpEndpointSync userProfileHttpEndpointSync;
    private final UsersCache usersCache;

    public FetchUserProfileUseCaseSync(UserProfileHttpEndpointSync userProfileHttpEndpointSync, UsersCache usersCache) {
        this.userProfileHttpEndpointSync = userProfileHttpEndpointSync;
        this.usersCache = usersCache;
    }

    public UseCaseResult fetchUserProfileSync(String userId) {
        EndpointResult result = userProfileHttpEndpointSync.fetchUserProfileSync(userId);

        switch (result.getStatus()) {
            case SUCCESS:
                usersCache.cacheUser(result.getUserProfile());
                return UseCaseResult.SUCCESS;
            case SERVER_ERROR:
                return UseCaseResult.NETWORK_ERROR;
            default:
                return UseCaseResult.FAILURE;
        }
    }
}
