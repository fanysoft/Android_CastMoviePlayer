package cz.vancura.castmediaplayer.model;

import android.util.Log;

/*
Main POJO object class hosting Movies objects
 */
public class MoviePOJO {

    private static String TAG = "myTAG-MoviePOJO";

    int movieId;
    String movieName;
    String movieUrl;
    String movieImageUrl;
    String movieDescription;
    String movieLicence;
    String movieDuration;
    int movieViews;

    // constructor
    public MoviePOJO(int movieId, String movieName, String movieUrl, String movieImageUrl, String movieDescription, String movieLicence, String movieDuration, int movieViews) {
        Log.d(TAG, "new MoviePOJO created " + movieId + " " + movieName);
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieUrl = movieUrl;
        this.movieImageUrl = movieImageUrl;
        this.movieDescription = movieDescription;
        this.movieLicence = movieLicence;
        this.movieDuration = movieDuration;
        this.movieViews = movieViews;
    }


    // getters setters

    public String getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(String movieDuration) {
        this.movieDuration = movieDuration;
    }

    public int getMovieViews() {
        return movieViews;
    }

    public void setMovieViews(int movieViews) {
        this.movieViews = movieViews;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieLicence() {
        return movieLicence;
    }

    public void setMovieLicence(String movieLicence) {
        this.movieLicence = movieLicence;
    }
}
