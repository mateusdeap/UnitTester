package com.example.testingdoublesfundamentals.fetchuserprofile;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.testingdoublesfundamentals.fetchuserprofile.FetchUserProfileUseCaseSync.*;
import static org.junit.Assert.*;

public class FetchUserProfileUseCaseSyncTest {

    public static final int USER_ID = 123456;
    public static final String USER_NAME = "Mateus Pereira";
    public static final UserProfile expectedProfile = new UserProfile(USER_ID, USER_NAME);

    private UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    private UsersCacheTd usersCacheTd;

    private FetchUserProfileUseCaseSync fetchUserProfileUseCaseSync;

    @Before
    public void setUp() throws Exception {
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        fetchUserProfileUseCaseSync = new FetchUserProfileUseCaseSync(
                userProfileHttpEndpointSyncTd,
                usersCacheTd
        );
    }

    // given fetchUserProfileSync was called with a userID, it passes this to the UserProfileHttpEndpointSync
    @Test
    public void givenFetchUserProfileSyncWasCalled_fetchUserProfileSync_passesUserIdToUserProfileHttpEndpoint() {
        fetchUserProfileUseCaseSync.fetchUserProfileSync(expectedProfile.getStringId());

        assertEquals(USER_ID, userProfileHttpEndpointSyncTd.userId);
    }

    // given fetch succeeds, the user profile is cached
    @Test
    public void givenFetchSucceeds_fetchUserProfileSync_cachesUserProfile() {
        fetchUserProfileUseCaseSync.fetchUserProfileSync(expectedProfile.getStringId());

        assertNotNull(usersCacheTd.getUser(USER_ID));
        assertEquals(expectedProfile.getId(), usersCacheTd.getUser(USER_ID).getId());
        assertEquals(expectedProfile.getName(), usersCacheTd.getUser(USER_ID).getName());
    }

    // given fetch succeeds, it returns success
    @Test
    public void givenFetchSucceeds_fetchUserProfileSync_returnsSuccess() {
        UseCaseResult result = fetchUserProfileUseCaseSync
                .fetchUserProfileSync(expectedProfile.getStringId());

        assertEquals(UseCaseResult.SUCCESS, result);
    }

    // given fetch profile fails with any error, it does not cache user profile
    @Test
    public void givenFetchFailsWithAnyError_fetchUserProfileSync_doesNotCacheUserProfile() {
        // Auth error
        userProfileHttpEndpointSyncTd.returnsAuthError = true;

        fetchUserProfileUseCaseSync.fetchUserProfileSync(expectedProfile.getStringId());

        assertNull(usersCacheTd.getUser(expectedProfile.getId()));

        userProfileHttpEndpointSyncTd.returnsAuthError = false;

        // Server error
        userProfileHttpEndpointSyncTd.returnsServerError = true;

        fetchUserProfileUseCaseSync.fetchUserProfileSync(expectedProfile.getStringId());

        assertNull(usersCacheTd.getUser(expectedProfile.getId()));

        userProfileHttpEndpointSyncTd.returnsServerError = false;

        // General error
        userProfileHttpEndpointSyncTd.returnsGeneralError = true;

        fetchUserProfileUseCaseSync.fetchUserProfileSync(expectedProfile.getStringId());

        assertNull(usersCacheTd.getUser(expectedProfile.getId()));

        userProfileHttpEndpointSyncTd.returnsGeneralError = false;
    }

    // given fetch profile fails with auth error, it returns failure
    @Test
    public void givenFetchFailsWithAuthError_fetchUserProfileSync_returnsFailure() {
        userProfileHttpEndpointSyncTd.returnsAuthError = true;

        UseCaseResult result = fetchUserProfileUseCaseSync
                .fetchUserProfileSync(expectedProfile.getStringId());

        assertEquals(UseCaseResult.FAILURE, result);
    }

    // given fetch profile fails with server error, it returns network error
    @Test
    public void givenFetchFailsWithServerError_fetchUserProfileSync_returnsNetworkError() {
        userProfileHttpEndpointSyncTd.returnsServerError = true;

        UseCaseResult result = fetchUserProfileUseCaseSync
                .fetchUserProfileSync(expectedProfile.getStringId());

        assertEquals(UseCaseResult.NETWORK_ERROR, result);
    }

    // given fetch profile fails with general error, it returns failure
    @Test
    public void givenFetchFailsWithGeneralError_fetchUserProfileSync_returnsFailure() {
        userProfileHttpEndpointSyncTd.returnsGeneralError = true;

        UseCaseResult result = fetchUserProfileUseCaseSync
                .fetchUserProfileSync(expectedProfile.getStringId());

        assertEquals(UseCaseResult.FAILURE, result);
    }

    //----------------------------------------------------------------------------------------------
    //------------------------------------- HELPER CLASSES -----------------------------------------
    //----------------------------------------------------------------------------------------------

    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {
        public int userId;
        public boolean returnsAuthError;
        public boolean returnsServerError;
        public boolean returnsGeneralError;

        @Override
        public EndpointResult fetchUserProfileSync(String userId) {
            this.userId = Integer.parseInt(userId);
            UserProfile user = new UserProfile();
            user.setId(this.userId);

            if (returnsAuthError)
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, user);
            if (returnsServerError)
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, user);
            if (returnsGeneralError)
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, user);

            user.setId(USER_ID);
            user.setName(USER_NAME);
            return new EndpointResult(EndpointResultStatus.SUCCESS, user);
        }
    }

    private static class UsersCacheTd implements UsersCache {

        private final List<UserProfile> users;

        public UsersCacheTd() {
            this.users = new ArrayList<>();
        }

        @Override
        public void cacheUser(UserProfile userProfile) {
            users.add(userProfile);
        }

        @Nullable
        @Override
        public UserProfile getUser(int userId) {
            for (UserProfile user : users) {
                if (user.getId() == userId)
                    return user;
            }

            return null;
        }
    }
}