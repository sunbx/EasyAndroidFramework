package com.pep.core.http;


import com.pep.core.model.BaseListModel;
import com.pep.core.model.JokeModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    @GET("getJoke")
    Call<BaseListModel<JokeModel>> getJoke(@Query("page") int page);
}