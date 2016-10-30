package arm.milagrous;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;


import arm.milagrous.dbConnection.Record;
import arm.milagrous.dbConnection.RecordCRUD;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapLongClickListener {

    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // test mock
/*
        final RecordCRUD crud = RecordCRUD.getInstance(this);
        crud.CreateRealm("RecordTable");

        final Record record = new Record ();
        record.setType("target");
        record.setCount(5);

        crud.createRecord(record, new RecordCRUD.RecordCreatedCallback() {
            @Override
            public void RecordCreated(boolean yesNo) {
                if (yesNo == true)
                    Toast.makeText(MapsActivity.this, record.getType() + " Created", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MapsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng stepanakert = new LatLng(39.8264417, 46.728725);
        Marker initialMarker = mMap.addMarker(new MarkerOptions().position(stepanakert).title("Ստեփանակերտ"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stepanakert));

    }


    @Override
    public void onMapLongClick(LatLng latLng) {


            float maxZoomLevel = mMap.getMaxZoomLevel();
            float curZoomLevel = mMap.getCameraPosition().zoom;

            if (curZoomLevel == maxZoomLevel) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                LayoutInflater layoutInflater
                        = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.record_popup, null, false);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);


                Button btnCancel = (Button)popupView.findViewById(R.id.cancel);
                btnCancel.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

            }

    }
}
