package cz.vancura.castmediaplayer.model.retrofit;

import java.util.List;

import cz.vancura.castmediaplayer.model.MoviePOJO;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
Retrofit interface - Http method GET or POST with 2nd part of Url
 */

public interface RetrofitAPIInterface {

    // GET bez URL params - response is List of RetrofitPOJO
    // connecting to https://www.vancura.cz/programing/Android/Demo/MediaPlayer2/rest_json.php
    @GET("/programing/Android/Demo/MediaPlayer2/rest_json.php") // Url konec
    Call<List<RetrofitPOJO>> getMovies();


    // POST insert - response is String
    // example https://www.vancura.cz/programing/Android/Demo/MediaPlayer2/update_views.php?id=1
    @POST("/programing/Android/Demo/MediaPlayer2/update_views.php") // Url konec
    Call<String> postViews(@Query("id") int id);


}
