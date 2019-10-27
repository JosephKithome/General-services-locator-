package com.example.sejjoh.gsls.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "content-Type:application/json",
                    "Authorization:key=AAAAm7DF71s:APA91bELzPm2FQzZNiRCLNQ0th-MUEt3i-PIU_eoX_tSP6Jybl1ye942kS-u4aHCmmzWm6i7AFpy4RIDhvqdizV_5uNtXBWAb3aW7MKcuPZ5xC3p34Nw8Vf_cd4P_RX_UOZFqr3nwZZH"
            }

    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
