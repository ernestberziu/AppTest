package com.threel.apptest;



import java.util.List;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET("users")
    Observable<JsonObjects> getTotalPages();
//    Call<JsonObjects> getTotalUsers();
    @GET("users?")
    Observable<JsonObjects>getPageUsers(@Query("page") String page);
    @GET("users?")
    Observable<JsonObjects.Users>getUsersById(@Query("id") String id);

}
