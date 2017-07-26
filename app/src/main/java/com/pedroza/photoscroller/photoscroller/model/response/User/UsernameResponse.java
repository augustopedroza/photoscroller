package com.pedroza.photoscroller.photoscroller.model.response.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsernameResponse {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("stat")
    @Expose
    private String stat;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}