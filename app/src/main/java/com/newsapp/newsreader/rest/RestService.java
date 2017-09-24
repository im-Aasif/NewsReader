package com.newsapp.newsreader.rest;

import com.newsapp.newsreader.model.ArticleResponse;
import com.newsapp.newsreader.model.SourceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by GLaDOS on 9/19/2017.
 */

public interface RestService {

    @GET("sources")
    Call<SourceResponse> getSources(@Query("language") String language);

    @GET("sources")
    Call<SourceResponse> getSourcesByCategory(@Query("category") String category);

    @GET("articles")
    Call<ArticleResponse> getArticles(@Query("apiKey") String apiKey, @Query("source") String source, @Query("sortBy") String sortBy);
}
