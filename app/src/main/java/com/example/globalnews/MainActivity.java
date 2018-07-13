package com.example.globalnews;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GlobalNewsAdapter.NewsOnClickHandler {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ACCESS_FINE_LOCATION = 16;

    private RecyclerView mRecyclerView;
    private GlobalNewsAdapter mGlobalNewsAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.recycler_view);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_activity_coordinator_layout);

        mGlobalNewsAdapter = new GlobalNewsAdapter(this, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mGlobalNewsAdapter);


        if (NetworkIsAvailable()) {

            printPlaceAndLikelihood();

            NewsViewModel model = ViewModelProviders.of(this).get(NewsViewModel.class);
            model.getNews().observe(this, new Observer<ArrayList<News>>() {
                @Override
                public void onChanged(@Nullable ArrayList<News> newsArrayList) {
                    showProgressBar();
                    mGlobalNewsAdapter.swapNewsArrayList(newsArrayList);
                    showRecyclerView();
                }
            });
        } else {
            hideProgressBarAndRecyclerView();
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.snackbar_taxt, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.retry_snackbar_action, new RetryListener(this));
            snackbar.show();

        }

    }

    private void printPlaceAndLikelihood() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        } else {
            PlaceDetectionClient placeDetectionClient = Places.getPlaceDetectionClient(this);
            Task<PlaceLikelihoodBufferResponse> placeResult  = placeDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    PlaceLikelihood likelihood = likelyPlaces.get(0);
                    Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(likelihood.getPlace().getLatLng().latitude, likelihood.getPlace().getLatLng().longitude, 1);
                        Address address = addressList.get(0);
                        Log.i(TAG, "Country Name is: " + address.getCountryName());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void showRecyclerView() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarAndRecyclerView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private boolean NetworkIsAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkState = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            networkState = true;
        }
        return networkState;
    }

    @Override
    public void onClickHandler(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    printPlaceAndLikelihood();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.i(TAG, String.format("Permission not Granted"));
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
