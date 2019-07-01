package com.example.nini.Nini.webService;

import com.example.nini.Nini.models.OtpGetResult;
import com.example.nini.Nini.models.OtpPostResults;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {
    @POST("Otp/SubscribeRequest")
    @FormUrlEncoded
    Call<OtpPostResults> registerUser(@Field("MobileNumber") String MobileNumber, @Field("ServiceId") int ServiceId, @Field("Channel") String Channel );

    @POST("Otp/SubscribeConfirm")
    @FormUrlEncoded

    Call <OtpPostResults> subscribeUser(@Field("ServiceId") String ServiceId, @Field("TransactionId") String TransactionId, @Field("Pin") String Pin );

    @POST("Otp/SubscribeConfirmViaCode")
    @FormUrlEncoded
    Call <OtpPostResults> subscribeUserViaCode (@Field("ServiceId") String ServiceId, @Field("TransactionId") String TransactionId, @Field("Pin") String Pin);

    @GET("Subscription/GetSubscriptionState")
    Call <OtpGetResult> getSubStatus (@Query("MobileNumber") String MobileNumber, @Query("ServiceId") int ServiceId  );

}
