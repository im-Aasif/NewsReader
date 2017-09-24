package com.newsapp.newsreader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.newsapp.newsreader.R;
import com.newsapp.newsreader.adapter.CategoryAdapter;
import com.newsapp.newsreader.model.Source;
import com.newsapp.newsreader.model.SourceResponse;
import com.newsapp.newsreader.rest.ApiUtils;
import com.newsapp.newsreader.rest.RestService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestService restService;
    private CategoryAdapter categoryAdapter;
    private String TAG = CategoryListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        prepareViews();
        prepareApi();
    }


    private void prepareViews() {
        recyclerView = (RecyclerView) findViewById(R.id.category_recycler_view);
        restService = ApiUtils.getRestService();

        categoryAdapter = new CategoryAdapter(this, new ArrayList<String>(0), new CategoryAdapter.CategoryItemListener() {
            @Override
            public void onCategoryClick(String category) {
                Intent intent = new Intent(CategoryListActivity.this, SourceListActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void prepareApi() {
        restService.getSources("en").enqueue(new Callback<SourceResponse>() {
            @Override
            public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                if (response.isSuccessful()) {
                    SourceResponse sourceResponse = response.body();
                    try {
                        ArrayList<String> categoryList = new ArrayList<>();
                        for (Source source : sourceResponse.getSources()) {
                            categoryList.add(source.getCategory().toUpperCase());
                        }

                        Set<String> distinctCategoryList = new HashSet<>(categoryList);
                        categoryAdapter.updateCategories(distinctCategoryList);
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
            public void onFailure(Call<SourceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
