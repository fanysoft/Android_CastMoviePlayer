package cz.vancura.castmediaplayer.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cz.vancura.castmediaplayer.model.MoviePOJO;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitAPIClient;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitAPIInterface;
import cz.vancura.castmediaplayer.model.retrofit.RetrofitPOJO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/*
MVVM ViewModel for MainActitivy - bussiness logic here is Http Work
 */
public class MainActivityViewModel extends ViewModel {

    private static String TAG = "myTAG-MainActivityViewModel";

    public static List<MoviePOJO> moviePOJOList = new ArrayList<>();

    // LiveData List
    private static MutableLiveData<List<MoviePOJO>> moviePOJOListLiveData; // List

    public MutableLiveData<List<MoviePOJO>> getMoviePOJOListLiveData() {
        if (moviePOJOListLiveData == null) {
            moviePOJOListLiveData = new MutableLiveData<>();
        }
        return moviePOJOListLiveData;
    }


    // LiveData Error
    private MutableLiveData<String> errorLiveData;

    public MutableLiveData<String> getErrorLiveData() {
        if (errorLiveData == null) {
            errorLiveData = new MutableLiveData<>();
        }
        return errorLiveData;
    }


    // download list of movies from REST Api
    public void HttpGetData() {
        Log.d(TAG, "HttpGetData");

        // empty list
        moviePOJOList.clear();

        // Retrofit interface
        RetrofitAPIInterface apiInterface = RetrofitAPIClient.getClient(false).create(RetrofitAPIInterface.class);

        // Retrofit HTTP call
        Call<List<RetrofitPOJO>> call = apiInterface.getMovies();
        call.enqueue(new Callback<List<RetrofitPOJO>>() {
            @Override
            public void onResponse(Call<List<RetrofitPOJO>> call, Response<List<RetrofitPOJO>> response) {

                Log.d(TAG, "HttpGetData - response code (200 is OK)=" + response.code());

                if (response.code() == 200) {
                    // ok

                    // try/catch while parsing data - server may change JSON structure
                    try{

                        for (RetrofitPOJO retrofitPOJO : response.body()) {
                            String data = retrofitPOJO.name + " " + retrofitPOJO.urlMovie + "\n";
                            Log.d(TAG,"response data loop - " +  data);

                            // create POJO
                            MoviePOJO moviePOJO = new MoviePOJO(retrofitPOJO.id, retrofitPOJO.name, retrofitPOJO.urlMovie, retrofitPOJO.urlIcon, retrofitPOJO.descr, retrofitPOJO.source, retrofitPOJO.duration, retrofitPOJO.views);
                            // add to List
                            moviePOJOList.add(moviePOJO);

                        }

                        // LiveData set - will triger GUI change in View
                        moviePOJOListLiveData.setValue(moviePOJOList);

                    }catch (Exception e){
                        String error = "Server ERROR " +  e.getLocalizedMessage();
                        Log.e(TAG,error);

                        // LiveData set - will triger GUI change in View
                        errorLiveData.setValue(error);

                    }

                }else{
                    //ng
                    String error = "Server response is not ok " + response.code();
                    Log.e(TAG, error);

                    // LiveData set - will triger GUI change in View
                    errorLiveData.setValue(error);
                }

            }

            @Override
            public void onFailure(Call<List<RetrofitPOJO>> call, Throwable t) {
                String error = "Server ERROR " +  t.getLocalizedMessage();
                Log.e(TAG, "onFailure " + error);

                // LiveData set - will triger GUI change in View
                errorLiveData.setValue(error);

                // Retrofit call cancel
                call.cancel();
            }
        });


    }


}
