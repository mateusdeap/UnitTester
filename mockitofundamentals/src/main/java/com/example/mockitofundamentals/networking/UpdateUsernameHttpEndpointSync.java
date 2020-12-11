package com.example.mockitofundamentals.networking;

public interface UpdateUsernameHttpEndpointSync {

    /**
     * Update user's username on the server
     * @return the aggregated result
     * @throws NetworkErrorException if operation failed due to network error
     */

    EndpointResult updateUsername(String userId, String userName) throws NetworkErrorException;

    enum EndpointResultStatus {
        SUCCESS,
        AUTH_ERROR,
        SERVER_ERROR,
        GENERAL_ERROR
    }

    class EndpointResult {
        private final EndpointResultStatus status;
        private final String userId;
        private final String userName;

        public EndpointResult(EndpointResultStatus status, String userId, String userName) {
            this.status = status;
            this.userId = userId;
            this.userName = userName;
        }

        public EndpointResultStatus getStatus() {
            return status;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }
    }
}
