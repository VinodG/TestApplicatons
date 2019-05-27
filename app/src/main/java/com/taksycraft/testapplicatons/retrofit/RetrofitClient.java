package com.taksycraft.testapplicatons.retrofit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RetrofitClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static Retrofit retrofit;

    public static Retrofit getClient(){
        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }

}
