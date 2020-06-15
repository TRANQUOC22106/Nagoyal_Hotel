package com.quoctran.nagoyalhotel.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.quoctran.nagoyalhotel.R;
import com.quoctran.nagoyalhotel.utils.FirebaseUtils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HotelDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLatLng;
    private GoogleMap mMap;
    private TextView titleToolbar;
    private TextView tv_nameHotel_hotelDetail;
    private TextView tv_descriptionHotel_hotelDetail;
    private TextView tv_priceHotel_hotelDetail;
    private TextView tv_locationHotel_hotelDetail;
    private Button btn_bookingBtn_hotelDetail;
    private ImageView imv_imageHotel_hotelDetail;
    private String nameLocation;
    private LatLng sydney;
    private String idHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpPreStart();

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
        receivedDataFromFirebase(googleMap);
    }

    private void receivedDataFromFirebase(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        idHotel = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("idHotel").toString());
        FirebaseUtils.getDocument("HotelList", idHotel).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    sydney = new LatLng((document.get("latitude") == null) ? 0 : Double.valueOf(Objects.requireNonNull(document.get("latitude")).toString()), (document.get("longitude") == null) ? 0 : Double.valueOf(Objects.requireNonNull(document.get("longitude")).toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    nameLocation = (document.get("nameHotel") == null) ? null : String.valueOf(document.get("nameHotel"));
                    mMap.addMarker(new MarkerOptions().position(sydney).title((document.get("nameHotel") == null) ? null : String.valueOf(document.get("nameHotel"))));
                    titleToolbar.setText((document.get("nameHotel") == null) ? null : String.valueOf(document.get("nameHotel")));
                    tv_nameHotel_hotelDetail.setText((document.get("nameHotel") == null) ? null : String.valueOf(document.get("nameHotel")));
                    tv_descriptionHotel_hotelDetail.setText((document.get("descriptionHotel") == null) ? null : String.valueOf(document.get("descriptionHotel")));
                    tv_locationHotel_hotelDetail.setText((document.get("locationHotel") == null) ? null : String.valueOf(document.get("locationHotel")));
                    tv_priceHotel_hotelDetail.setText((document.get("priceHotel") == null) ? null : String.valueOf(document.get("priceHotel")));
                    btn_bookingBtn_hotelDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((document.get("link") == null) ? null : String.valueOf(document.get("link"))));
                            startActivity(browserIntent);
                        }
                    });

                    Picasso.get().load((document.get("imageHotel") == null) ? null : String.valueOf(document.get("imageHotel"))).into(imv_imageHotel_hotelDetail);
                } else {

                }
            }
        });
        mMap.setMinZoomPreference(13f);
    }

    private void setUpPreStart() {
        Toolbar mToolbar = findViewById(R.id.toolBar_hotelDetail);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        titleToolbar = mToolbar.findViewById(R.id.titleToolAppBar);
        tv_nameHotel_hotelDetail = findViewById(R.id.tv_nameHotel_hotelDetail);
        tv_descriptionHotel_hotelDetail = findViewById(R.id.tv_descriptionHotel_hotelDetail);
        tv_locationHotel_hotelDetail = findViewById(R.id.tv_locationHotel_hotelDetail);
        tv_priceHotel_hotelDetail = findViewById(R.id.tv_priceHotel_hotelDetail);
        btn_bookingBtn_hotelDetail = findViewById(R.id.btn_bookingBtn_hotelDetail);
        imv_imageHotel_hotelDetail = findViewById(R.id.imv_imageHotel_hotelDetail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
