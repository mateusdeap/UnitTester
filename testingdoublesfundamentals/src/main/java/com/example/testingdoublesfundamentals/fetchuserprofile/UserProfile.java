package com.example.testingdoublesfundamentals.fetchuserprofile;

public class UserProfile {

    private int id;
    private String name;

    public UserProfile(int userId, String userName) {
        this.id = userId;
        this.name = userName;
    }

    public UserProfile() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStringId() {
        return String.valueOf(this.id);
    }
}
