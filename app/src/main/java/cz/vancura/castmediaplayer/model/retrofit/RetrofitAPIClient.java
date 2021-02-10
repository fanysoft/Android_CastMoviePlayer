package cz.vancura.castmediaplayer.model.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitAPIClient {

    public static Retrofit retrofit = null;
    private static String URL = "https://www.vancura.cz"; // API main domain only

    static public Retrofit getClient(boolean forStringResponse) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (forStringResponse) {
            // need to use ScalarsConverterFactory if Response is String

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    //.addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build();



        }else{
            // need to use GsonConverterFactory if Response is JSON

            retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        }

        return retrofit;
    }







}
