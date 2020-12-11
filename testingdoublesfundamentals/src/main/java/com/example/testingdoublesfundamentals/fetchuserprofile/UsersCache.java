package com.example.testingdoublesfundamentals.fetchuserprofile;

import org.jetbrains.annotations.Nullable;

public interface UsersCache {

    void cacheUser(UserProfile userProfile);

    @Nullable
    UserProfile getUser(int userId);
}
