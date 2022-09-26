package com.busybees.little_bee.network.sync;

import com.busybees.little_bee.data.SendMessage.SendMessageModel;
import com.busybees.little_bee.data.SendMessage.SendMessageObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface CallSendMessageSync {
    @POST
    Call<SendMessageModel> callSendMessage(@Url String url, @Header("Authorization") String header, @Body SendMessageObject object);
}
