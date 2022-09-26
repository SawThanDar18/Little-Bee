package com.busybees.little_bee.data.SendMessage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SendMessageObject implements Serializable {

    @SerializedName("address")
    @Expose
    private List<String> receivePhoneNumber;

    @SerializedName("senderAddress")
    @Expose
    private String senderAddress;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("notifyURL")
    @Expose
    private String notifyURL;

    @SerializedName("senderName")
    @Expose
    private String senderName;

    public List<String> getReceivePhoneNumber() {
        return receivePhoneNumber;
    }

    public void setReceivePhoneNumber(List<String> receivePhoneNumber) {
        this.receivePhoneNumber = receivePhoneNumber;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
