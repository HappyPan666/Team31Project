package edu.northeastern.team31project.ChatAndNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface WebAPI {
    @Headers(
            {       "Content-Type:application/json",
                    "Authorization:key=AAAATbv4Qeg:APA91bF3KgKkILEn4kZHsBNKWdxbPhuHitjET0ut3jVRMZOpDdZlE6uaRkNgVDYX39_GtxPR5PGqQ5k0vhcS5Q3qO_NI_89hbwy5Hf7XsXmXplQPNXFK0asoPkIlpFkAJlxIZ7PZwSts"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendAndNotify(@Body Send body);
}