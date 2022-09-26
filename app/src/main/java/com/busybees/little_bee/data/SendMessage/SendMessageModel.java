package com.busybees.little_bee.data.SendMessage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SendMessageModel implements Serializable {

    @SerializedName("resourceReference")
    @Expose
    private ResourceURLObject resourceURLObject;

    public ResourceURLObject getResourceURLObject() {
        return resourceURLObject;
    }

    public void setResourceURLObject(ResourceURLObject resourceURLObject) {
        this.resourceURLObject = resourceURLObject;
    }
}
