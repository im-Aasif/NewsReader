package com.newsapp.newsreader.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.newsapp.newsreader.R;
import com.newsapp.newsreader.model.ArticleResponse;
import com.newsapp.newsreader.model.SourceResponse;
import com.newsapp.newsreader.rest.ApiUtils;
import com.newsapp.newsreader.rest.RestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrialActivity extends AppCompatActivity {

    private RestService restService;
    private String TAG = TrialActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial);

        restService = ApiUtils.getRestService();

        restService.getSources("en").enqueue(new Callback<SourceResponse>() {

            @Override
            public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                if (response.isSuccessful()) {
                    SourceResponse sourceResponse = response.body();
                    Log.d(TAG, "onResponse: " + response.toString());
                    Log.d(TAG, "onResponse: " + sourceResponse.getStatus());
                    String src = sourceResponse.getSources().get(0).getId();
                    restService.getArticles(ApiUtils.apiKey, src, "").enqueue(new Callback<ArticleResponse>() {
                        @Override
                        public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                            if (response.isSuccessful()) {
                                ArticleResponse articleResponse = response.body();

                                Log.d(TAG, "onResponse: " + response.raw());
                                Log.d(TAG, "onResponse: " + articleResponse.getSource());
                            } else {
                                int statusCode = response.code();
                                Log.d(TAG, "onResponse: StatusCode: " + statusCode);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArticleResponse> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                        }
                    });
                } else {
                    int statusCode = response.code();
                    Log.d(TAG, "onResponse: StatusCode: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<SourceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }

        });


    }
}
