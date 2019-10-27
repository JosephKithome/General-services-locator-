package com.example.sejjoh.gsls.Remote;

import com.example.sejjoh.gsls.Model.MyPlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by sejjoh on 04/02/2019 .
 */

public interface IGoogleAPIService {


    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);



}
