package com.example.nini.Nini.models;
import com.google.gson.annotations.SerializedName;
public class OtpPostResults {

    public Boolean getSuccessful() {
        return IsSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        IsSuccessful = successful;
    }

    @SerializedName("IsSuccessful")
    private Boolean IsSuccessful;
    @SerializedName("Message")
    private String Message;
    @SerializedName("Result")
    private Long Result;

    public Boolean getIsSuccessful() {
        return IsSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        IsSuccessful = isSuccessful;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Long getResult() {
        return Result;
    }

    public void setResult(Long result) {
        Result = result;
    }


}
