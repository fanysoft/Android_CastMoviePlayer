package cz.vancura.castmediaplayer.model.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RetrofitPOJO {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("url_movie")
    @Expose
    public String urlMovie;

    @SerializedName("url_icon")
    @Expose
    public String urlIcon;

    @SerializedName("descr")
    @Expose
    public String descr;

    @SerializedName("source")
    @Expose
    public String source;

    @SerializedName("duration")
    @Expose
    public String duration;

    @SerializedName("views")
    @Expose
    public int views;


}
