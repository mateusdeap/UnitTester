package com.example.testdrivendevelopment;

import org.jetbrains.annotations.NotNull;

public interface GetReputationUseCaseSync {
    @NotNull UseCaseResult getReputation();

    enum Status {
        SUCCESS,
        FAILURE
    }

    class UseCaseResult {
        private final Status status;
        private final int reputation;

        public UseCaseResult(Status status, int reputation) {
            this.status = status;
            this.reputation = reputation;
        }

        public int getReputation() {
            return this.reputation;
        }

        public Status getStatus() {
            return this.status;
        }
    }
}
