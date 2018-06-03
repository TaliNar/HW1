package com.example.tali.hw1;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tali.hw1.db.Player;
import com.example.tali.hw1.viewmodel.PlayerViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class FragmentScoreMap extends Fragment implements OnMapReadyCallback{
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private PlayerViewModel playerViewModel;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private Marker currentLocationMarker = null;
    private SupportMapFragment mapFragment;
    private String name;
    View view;

    public FragmentScoreMap() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_maps, container, false);
        Bundle data = getActivity().getIntent().getExtras();
        name = data.getString(EntryActivity.NAME);

        //mLastLocation = getArguments().getParcelable("last Location");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location.getAccuracy() <= 50)
                    mLocationManager.removeUpdates(mLocationListener);
                if(currentLocationMarker != null)
                    currentLocationMarker.remove();
                addMarkerCurrentLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    requestLocation();
                return;
        }
    }

    public Location requestLocation() {
        Location location = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, 1);
                return null;
            }
        } else {
            mLocationManager.requestLocationUpdates("gps", LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }

/*    public void addMarkers(Player player) {
        Address address = player.getAddress();
        LatLng playerLocation = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(playerLocation).title(player.toString()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(playerLocation, 12));
    }*/

    private void addMarkerCurrentLocation(Location location) {
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        //Player me = null;
        Location currentLocation = requestLocation();
        if(currentLocation != null) {
            //Address address = convertToAddress(currentLocation);
            //me = new Player(name, 100, address);
            addMarkerCurrentLocation(currentLocation);
        }
        showPlayers();
    }

    private Address convertToAddress(Location location){
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showPlayers() {
        // start the AsyncTask, passing the Activity context
        // in to a custom constructor
        new MyTask(getActivity(), playerViewModel, mMap).execute();
    }

    private static class MyTask extends AsyncTask<Void, Void, List<Player>> {

        private WeakReference<Activity> contextReference;
        private PlayerViewModel playerViewModel;
        private GoogleMap mMap;

        // only retain a weak reference to the activity
        public MyTask(Activity activity, PlayerViewModel playerViewModel, GoogleMap mMap) {
            contextReference = new WeakReference<>(activity);
            this.playerViewModel = playerViewModel;
            this.mMap = mMap;
        }

        @Override
        protected List<Player> doInBackground(Void... params) {
            List<Player> players = playerViewModel.getListOfPlayers();
            return players;
        }

        @Override
        protected void onPostExecute(List<Player> players) {

            // get a reference to the activity if it is still there
            Activity activity = contextReference.get();
            if (activity == null || activity.isFinishing()) return;

            // modify the UI
            for(int i = 0; i < players.size(); i++) {
                Player currentPlayer = players.get(i);
                Address address = currentPlayer.getAddress();
                LatLng playerLocation = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(playerLocation).title(currentPlayer.toString()));
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(playerLocation, 12));
            }
        }
    }

}



