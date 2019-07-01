package com.example.nini.Nini.models;

import com.google.gson.annotations.SerializedName;

public class SubScribeUser {
    @SerializedName("ServiceId")
    private int ServiceId;
    @SerializedName("TransactionId")
    private int TransactionId;
    @SerializedName("Pin")
    private int Pin;

    public int getServiceId() {
        return ServiceId;
    }

    public void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }

    public int getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(int transaction) {
        TransactionId = transaction;
    }

    public int getPin() {
        return Pin;
    }

    public void setPin(int pin) {
        Pin = pin;
    }

}
