package com.example.testingdoublesfundamentals.fetchuserprofile;

public interface UserProfileHttpEndpointSync {

    EndpointResult fetchUserProfileSync(String userId);

    enum EndpointResultStatus {
        SUCCESS,
        AUTH_ERROR,
        SERVER_ERROR,
        GENERAL_ERROR
    }

    class EndpointResult {
        private final EndpointResultStatus status;
        private final UserProfile userProfile;

        public EndpointResult(EndpointResultStatus status, UserProfile userProfile) {
            this.status = status;
            this.userProfile = userProfile;
        }

        public EndpointResultStatus getStatus() {
            return status;
        }

        public UserProfile getUserProfile() {
            return userProfile;
        }
    }
}
