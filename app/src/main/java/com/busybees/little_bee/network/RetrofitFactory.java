package com.busybees.little_bee.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static Retrofit retrofit;

    public static Retrofit connector(){
        if(retrofit==null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).addInterceptor(interceptor);
            OkHttpClient okHttpClient = okClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Response response = chain.proceed(chain.request());
                    return response;
                }
            }).build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            Retrofit.Builder builder = new Retrofit.Builder();
            builder.addConverterFactory(GsonConverterFactory.create());

            builder.client(client);
            builder.baseUrl("https://dev.busybeesexpertservice.com/");

            retrofit = builder.build();
        }
        return retrofit;


    }
}
