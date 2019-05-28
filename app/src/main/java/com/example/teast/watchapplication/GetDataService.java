package com.example.teast.watchapplication;

import com.example.teast.watchapplication.Data.Episodes;
import com.example.teast.watchapplication.Data.PopularShows;
import com.example.teast.watchapplication.Data.SingleShow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/3/tv/popular")
    Call<PopularShows> getAllShows(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("/3/trending/tv/day")
    Call<PopularShows> getTrendingShows(
            @Query("api_key") String apiKey
            );

    @GET("/3/search/tv")
    Call<PopularShows> getSearchedShows(
            @Query("api_key") String apiKey,
            @Query("query") String query
    );

    @GET("/3/tv/{id}")
    Call<SingleShow> getSingleShow(
            @Path("id") int showId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("/3/tv/{tv_id}/season/{season_number}")
    Call<Episodes> getEpisodes(
            @Path("tv_id") int tvId,
            @Path("season_number") int seasonNumber,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );


}
