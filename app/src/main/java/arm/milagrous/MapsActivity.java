package arm.milagrous;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
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


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, OnMapLongClickListener, NumberPicker.OnValueChangeListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    String[] data = {"one", "two", "three", "four", "five"};
    String selectedObjectType;
    final RecordCRUD crud = RecordCRUD.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        crud.CreateRealm("RecordTable");



//        Log.d(TAG, "RealmDB INITIAL size is:" + crud.getAllRecords().size());
//
//        crud.deleteAllRecords(new RecordCRUD.OnAllItemsDeletedListener() {
//
//            @Override
//            public void onAllItemsDeleted(boolean yesNo) {
//
//                if (yesNo == true) {
//                    Log.d(TAG, "Records Deleted");
//                    Log.d(TAG, "RealmDB size after Records Delete is:" + crud.getAllRecords().size());
//                } else {
//                    Log.d(TAG, "records NOT Deleted");
//                }
//
//            }
//        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).

                addApi(AppIndex.API).build();

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
        mMap.setOnMarkerClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng stepanakert = new LatLng(39.8264417, 46.728725);
        Marker initialMarker = mMap.addMarker(new MarkerOptions().position(stepanakert).title("Ստեփանակերտ"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stepanakert));


        for(Record record : crud.getAllRecords()){

            LatLng objectLocation = new LatLng(record.getLatitude(), record.getLongitude());
            Marker objectMarker = mMap.addMarker(new MarkerOptions().position(objectLocation).title(record.getType()));

        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        float maxZoomLevel = mMap.getMaxZoomLevel();
        float curZoomLevel = mMap.getCameraPosition().zoom;

//        if (curZoomLevel == maxZoomLevel) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        LayoutInflater layoutInflater
                = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.record_popup, null, false);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        initPopupWindow(popupWindow, latLng);



    }
    private void initPopupWindow(final PopupWindow popupWindow, Marker marker) {


        initPopupWindow(popupWindow, marker.getPosition());
    }

    private void initPopupWindow(final PopupWindow popupWindow, final LatLng latLng) {

        final LatLng objectLocation = latLng;
        ///////////////////SPINNER////////////////////////////////

        Spinner spinner = (Spinner) popupWindow.getContentView().findViewById(R.id.spinner);
        if (spinner == null) {
            Log.i(TAG, "spinner is null");
        } else {
            Log.i(TAG, "spinner is NOT NULL");
        }


        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.objectsarray, android.R.layout.simple_spinner_item);

        if (adapter == null) {
            Log.i(TAG, "adapter is null");
        } else {
            Log.i(TAG, "adapter is NOT NULL");
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View popupView,
                                       int position, long id) {
                Log.d(TAG, "Selected element ID is:" + id);
                selectedObjectType = getResources().getStringArray(R.array.objectsarray)[(int) id];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Log.d(TAG, "Selected element is:" + spinner.getSelectedItem());

///////////////////////////////////

        ////////////////Number Picker/////////////////
        Button b1 = (Button) popupWindow.getContentView().findViewById(R.id.buttonIncrease);
        Button b2 = (Button) popupWindow.getContentView().findViewById(R.id.buttonDecrease);
        final NumberPicker np = (NumberPicker) popupWindow.getContentView().findViewById(R.id.number);
        np.setMaxValue(100); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                np.setValue(np.getValue() + 1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                np.setValue(np.getValue() - 1);
            }
        });

        popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);

        Button btnCancel = (Button) popupWindow.getContentView().findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        Button btnSave = (Button) popupWindow.getContentView().findViewById(R.id.save);
        btnSave.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Record record = new Record();
                record.setType(selectedObjectType);
                record.setCount(np.getValue());
                record.setLatitude(objectLocation.latitude);
                record.setLongitude(objectLocation.longitude);
                //record.setAltitude(ALTITUDE);

                Log.d(TAG, String.valueOf(objectLocation.latitude));
                Log.d(TAG, String.valueOf(objectLocation.longitude));

                crud.createRecord(record, new RecordCRUD.RecordCreatedCallback() {
                    @Override
                    public void RecordCreated(boolean yesNo) {
                        if (yesNo == true) {
                            Marker initialMarker = mMap.addMarker(new MarkerOptions().position(objectLocation).title(selectedObjectType));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(objectLocation));
                            Log.d(TAG, "object1 created");
                        } else {
                            Log.d(TAG, "object1 NOT created");
                        }
                    }
                });
                popupWindow.dismiss();

            }
        });


        Button btnDelete = (Button) popupWindow.getContentView().findViewById(R.id.deleteObject);
        btnDelete.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                Record record = new Record();
                record.setLatitude(latLng.latitude);
                record.setLongitude(latLng.longitude);
                //Log.d(TAG, String.valueOf(record.getLatitude()));
//                    Log.d(TAG, String.valueOf(record.getLongitude()));
                crud.deleteRecord(record, new RecordCRUD.RecordDeletedCallback() {

                    @Override
                    public void RecordIsDeleted(boolean yesNo) {

                        if (yesNo == true) {
                            Log.d(TAG, "Marker Deleted");
                            Log.d(TAG, "RealmDB size after Records Delete is:" + crud.getAllRecords().size());
                        } else {
                            Log.d(TAG, "records NOT Deleted");
                            Log.d(TAG, "RealmDB size is:" + crud.getAllRecords().size());
                        }
                    }
                });

                popupWindow.dismiss();
//                mMap.moveCamera(CameraUpdateFactory.zoomIn());
//                mMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });


    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        LayoutInflater layoutInflater
                = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = layoutInflater.inflate(R.layout.record_popup, null, false);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        LatLng markerLocation = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        Log.d(TAG, String.valueOf(marker.getPosition().latitude));
        Log.d(TAG, String.valueOf(marker.getPosition().longitude));

        marker.remove();

        initPopupWindow(popupWindow, markerLocation);

        return true; }
}
