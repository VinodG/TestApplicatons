package com.taksycraft.testapplicatons.retrofit;

import java.util.Vector;

import retrofit.Call;
import retrofit.http.GET;

public interface GetService {
    @GET("/photos")
    Call<Vector<RecordDO>> getAllRecords();
}
