package arm.milagrous;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import arm.milagrous.dbConnection.Record;
import arm.milagrous.dbConnection.RecordCRUD;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

        // Add a marker in Sydney and move the camera
        LatLng stepanakert = new LatLng(39.8264417,46.728725);
        mMap.addMarker(new MarkerOptions().position(stepanakert).title("Ստեփանակերտ"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stepanakert));
    }
}
