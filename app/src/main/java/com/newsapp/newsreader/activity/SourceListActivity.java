package com.newsapp.newsreader.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.newsapp.newsreader.R;
import com.newsapp.newsreader.adapter.SourceAdapter;
import com.newsapp.newsreader.model.Source;
import com.newsapp.newsreader.model.SourceResponse;
import com.newsapp.newsreader.rest.ApiUtils;
import com.newsapp.newsreader.rest.RestService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceListActivity extends AppCompatActivity {

    private SourceAdapter sourceAdapter;
    private RecyclerView recyclerView;
    private RestService restService;
    private String TAG = SourceListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_list);
        prepareViews();
        prepareApi();
    }

    private void prepareViews() {
        recyclerView = (RecyclerView) findViewById(R.id.src_recycler_view);
        restService = ApiUtils.getRestService();

        sourceAdapter = new SourceAdapter(this, new ArrayList<Source>(0), new SourceAdapter.SourceItemListener() {
            @Override
            public void onPostClick(String id) {
                Toast.makeText(SourceListActivity.this, "Source is " + id, Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(sourceAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void prepareApi() {
        restService.getSources("en").enqueue(new Callback<SourceResponse>() {
            @Override
            public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                if (response.isSuccessful()) {
                    SourceResponse sourceResponse = response.body();
                    try {
                        sourceAdapter.updateSources(sourceResponse.getSources());
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
