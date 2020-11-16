package com.med.com.drawing;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by younes on 07/05/2018.
 */

public interface RetrofitConnections {
    @POST("{access_Token}")
    Call<RecievedData> Login(@Path("access_Token") String accessToken);
    //pv6l7-1528588604/post
    @POST("public/api/v1/login")
    Call<RecievedData> getData(@Body LoginSentData user);

    @POST("public/api/v1/games")
    Call<GameInfoResponse> sendGameInfo(@Body GameInfo gameinfo,@Header("Authorization") String authHeader);

    @POST("public/api/v1/userAssignments")
    Call<Assignments> getOrgKids(@Body OrgKidsRequest orgKidsrequest, @Header("Authorization") String authHeader);
    @GET("ServerPaths")
    Call<PathsClass> getSeverPaths();

}