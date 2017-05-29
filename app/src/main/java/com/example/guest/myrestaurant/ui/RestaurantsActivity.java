package com.example.guest.myrestaurant.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.guest.myrestaurant.R;
import com.example.guest.myrestaurant.adapters.RestaurantListAdpater;
import com.example.guest.myrestaurant.models.Restaurant;
import com.example.guest.myrestaurant.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsActivity extends AppCompatActivity {
  public static final String TAG = RestaurantsActivity.class.getSimpleName();

  @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
  private RestaurantListAdpater mAdapter;

  public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restaurants);
    ButterKnife.bind(this);

    Intent intent = getIntent();
    String location = intent.getStringExtra("location");

    getRestaurants(location);
  }

  public void getRestaurants(String location) {
    final YelpService yelpService = new YelpService();
    yelpService.findRestaurants(location, new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        e.printStackTrace();
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        mRestaurants = yelpService.processResults(response);

        RestaurantsActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mAdapter = new RestaurantListAdpater(getApplicationContext(), mRestaurants);
            mRecyclerView.setAdapter(mAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantsActivity.this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
          }
        });
      }
    });
  }

}
