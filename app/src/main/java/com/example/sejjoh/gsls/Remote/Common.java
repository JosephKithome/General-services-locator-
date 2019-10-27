package com.example.sejjoh.gsls.Remote;


import com.bumptech.glide.request.Request;
import com.example.sejjoh.gsls.Model.Results;

public class Common {

    public static Request currentRequest;
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static Results currentResults;

    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }

}
