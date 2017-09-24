package com.newsapp.newsreader.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.newsapp.newsreader.R;
import com.newsapp.newsreader.adapter.ArticleAdapter;
import com.newsapp.newsreader.model.Article;
import com.newsapp.newsreader.model.ArticleResponse;
import com.newsapp.newsreader.model.Source;
import com.newsapp.newsreader.rest.ApiUtils;
import com.newsapp.newsreader.rest.RestService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleListActivity extends AppCompatActivity {

    private String TAG = ArticleListActivity.class.getSimpleName();
    private ArticleAdapter articleAdapter;
    private RecyclerView recyclerView;
    private RestService restService;
    private Source source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        source = getIntent().getParcelableExtra("srcObject");

        if (getSupportActionBar() != null) {
            if (!source.getName().isEmpty()) {
                getSupportActionBar().setTitle(source.getName());
            }
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prepareViews();
        prepareApi();
    }

    private void prepareViews() {
        restService = ApiUtils.getRestService();
        recyclerView = (RecyclerView) findViewById(R.id.article_recycler_view);

        articleAdapter = new ArticleAdapter(this, new ArrayList<Article>(0), new ArticleAdapter.ArticleItemListener() {
            @Override
            public void onArticleClick(Article article) {
//                Toast.makeText(ArticleListActivity.this, "Article is " + article.getTitle(), Toast.LENGTH_SHORT).show();
                prepareCustomTab(article.getUrl());
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(articleAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
    }

    private void prepareCustomTab(String url) {
        Uri uri = Uri.parse(url);

        // create an intent builder
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // Begin customizing
        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        // set start and exit animations
        //intentBuilder.setStartAnimations(this, android.R.anim.f, android.R.anim.slide_in_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // launch the url
        customTabsIntent.launchUrl(ArticleListActivity.this, uri);
    }

    private void prepareApi() {
        restService.getArticles(ApiUtils.apiKey, source.getId(), "").enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (response.isSuccessful()) {
                    ArticleResponse articleResponse = response.body();
                    try {
                        articleAdapter.updateArticles(articleResponse.getArticles());
                        Log.d(TAG, "onResponse: Sources loaded from API");
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getLocalizedMessage());
                    }
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
