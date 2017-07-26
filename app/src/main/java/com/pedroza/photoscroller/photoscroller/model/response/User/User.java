package com.pedroza.photoscroller.photoscroller.model.response.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pedroza on 7/24/17.
 */

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nsid")
    @Expose
    private String nsid;
    @SerializedName("username")
    @Expose
    private Username username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNsid() {
        return nsid;
    }

    public void setNsid(String nsid) {
        this.nsid = nsid;
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username username) {
        this.username = username;
    }
}
