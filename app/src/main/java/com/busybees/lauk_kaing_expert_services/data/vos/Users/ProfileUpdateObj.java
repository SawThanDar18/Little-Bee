package com.busybees.lauk_kaing_expert_services.data.vos.Users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUpdateObj {

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("name")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

<<<<<<< HEAD
=======
    /*@SerializedName("image")
    @Expose
    private String image;*/

>>>>>>> api/edit_user_profile_image
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

