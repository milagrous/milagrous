package arm.milagrous.dbConnection;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by movses on 10/8/16.
 */
public class RecordCRUD {

    private Context context;
    private Realm realm;
    private static final String TAG = "RecordCRUDClass";

    private RecordCRUD () {

    }

  public boolean CreateRealm (String name) {
        if (!(name == null)) {
            realm = RealmManager.getInstance (context).createRealm (name);
            Log.d(TAG, "Realm path" +  realm.getPath());
            return true;
        }
        return false;
    }

    /**
     * @param context is the application context
     * @return an instance of <b>RecordCRUD</b>
     */
    public static RecordCRUD getInstance (Context context) {
        RecordCRUD recordCRUD = new RecordCRUD ();
        recordCRUD.context = context;

        return recordCRUD;
    }


    /**
     * @param record          is the Record to be created
     * @param createdCallback is a callback to check if Record is created. The callback operation is
     *                        handled wheree the method is called.
     * @return
     */
    public boolean createRecord (Record record, RecordCreatedCallback createdCallback) {



        if (isExist(getAllRecords(), record)) {
            createdCallback.RecordCreated(false);
            Log.d(TAG, "DB record exist");
            return false;
        } else {

            realm.beginTransaction();
            Record newRecord = realm.createObject(Record.class);
            newRecord.setType(record.getType());
            newRecord.setCount(record.getCount());

            newRecord.setLatitude(record.getLatitude());
            newRecord.setLongitude(record.getLongitude());
            newRecord.setAltitude(record.getAltitude());

            realm.commitTransaction();
            createdCallback.RecordCreated(true);

            return true;
        }
    }

    /**
     * @return list of all <h3>Records</h3>
     */
    public List<Record> getAllRecords () {
        RealmResults<Record> allRecords = realm.where (Record.class).findAll ();
        List<Record> Records = new ArrayList<>(allRecords);

        return Records;
    }

    /**
     * @param record     is the Record to be deleted and the
     * @param callback is for checking for success or failure of operation
     * @return <h3><i>true</i></h3> if Record is successfully deleted and <h3><i>false</i></h3> if otherwise
     */
    public boolean deleteRecord (Record record, RecordDeletedCallback callback) {
        if (isExist (getAllRecords (), record)) {
            realm.beginTransaction ();
            RealmResults<Record> realmResults = realm.where (Record.class)
                    .equalTo("longitude", record.getLongitude())
                    .equalTo("latitude", record.getLatitude())
                    .findAll();

            Log.d(TAG, "RealmResults size is" + realmResults.size());
            if (!realmResults.isEmpty ()) {
                Log.d(TAG, "realmResults is NOT Empty");
                for (int i = 0; i < realmResults.size (); i++) {
                    realmResults.get (i).deleteFromRealm ();
                }
            }
            realm.commitTransaction ();
            callback.RecordIsDeleted(true);

            return true;
        }
        callback.RecordIsDeleted(false);

        return false;
    }

    /**
     * @param record                 is the current Record to be updated
     * @param type                   is the name parameter to be used in updating the current Record
     * @param count                  is the address to be used in updating the current Record
     * @param updateCompleteListener is a listener callback for checking if the update is successful
     * @return boolean statement of <h3><i>true</i></h3> if update is successful and <h3><i>false</i></h3> if otherwise
     */
    public boolean updateRecord (Record record, String type, Integer count, Double longitute,
                                 Double lattitude, /*Long altitude,*/ OnUpdateCompleteListener updateCompleteListener) {
        if (isExist (getAllRecords (), record)) {
            Record RecordToBeEdited = realm.where (Record.class)
                    .equalTo ("type", record.getType()).findFirst ();
            realm.beginTransaction ();
            RecordToBeEdited.setType(type);
            if (count > 0) {
                RecordToBeEdited.setCount(count);
            }
            RecordToBeEdited.setLongitude(longitute);
            RecordToBeEdited.setLatitude(lattitude);
            //RecordToBeEdited.setAltitude(altitude);

            realm.commitTransaction ();
            updateCompleteListener.onUpdate (true);

            return true;
        }
        updateCompleteListener.onUpdate (false);

        return false;
    }

    /**
     * @param type                      name param used in finding the Record in question
     * @param RecordFoundCompleteListener is a callback listener for validating Record found
     * @return the Record if not <b><i>null</i></i></b>
     */
    public Record getRecord (String type, RecordFoundCompleteListener RecordFoundCompleteListener) {
        if (!TextUtils.isEmpty(type)) {
            realm.beginTransaction ();
            Record record = realm.where (Record.class).equalTo ("type", type).findFirst ();
            realm.commitTransaction ();
            RecordFoundCompleteListener.onRecordFound (record);

            return record;
        }

        return null;
    }


    /**
     * @param marker  coordinates param used in finding the Record
     * @param RecordFoundCompleteListener is a callback listener for validating Record found
     * @return the Record if not <b><i>null</i></i></b>
     */
    public Record getRecord(LatLng marker,
                            RecordFoundCompleteListener RecordFoundCompleteListener) {
            realm.beginTransaction ();
            Record record = realm.where (Record.class).
                    equalTo("latitude", marker.latitude).equalTo("longitude", marker.longitude).findFirst();

            realm.commitTransaction ();
            RecordFoundCompleteListener.onRecordFound (record);

            return record;
    }

    /**
     * @param records is the list of Records in the database
     * @param record  is the Record to confirm its existence in the database
     * @return <h3><i>true</i></h3> if Record found and <h3><i>false</i></h3> if otherwise
     */
    private boolean isExist(List<Record> records, Record record) {
        for (int i = 0; i < records.size(); i++) {
            Record record1 = records.get(i);
            Log.d(TAG, "RECORD");
            Log.d(TAG, String.valueOf(record.getLatitude()));
            Log.d(TAG, String.valueOf(record.getLongitude()));

            Log.d(TAG, "RECORD1");
            Log.d(TAG, String.valueOf(record1.getLatitude()));
            Log.d(TAG, String.valueOf(record1.getLongitude()));

            if ((record1.getLatitude() == record.getLatitude()) && (record1.getLongitude() == record.getLongitude())) {
                Log.d(TAG, "RealmDB record EXIST");
                return true;
            }
        }
        return false;
    }

    /**
     * @param allItemsDeletedListener is a callback to confirm all items are deleted
     */
    public void deleteAllRecords (OnAllItemsDeletedListener allItemsDeletedListener) {
        if (realm != null) {
            realm.beginTransaction ();
            realm.deleteAll ();
            realm.commitTransaction ();
            allItemsDeletedListener.onAllItemsDeleted (true);
        } else {
            allItemsDeletedListener.onAllItemsDeleted (false);
        }
    }

    public interface RecordCreatedCallback {
         void RecordCreated (boolean yesNo);
    }

    public interface RecordDeletedCallback {
         void RecordIsDeleted (boolean yesNo);
    }

    public interface OnUpdateCompleteListener {
         void onUpdate (boolean yes);
    }

    public interface RecordFoundCompleteListener {
         void onRecordFound (Record Record);
    }

    public interface OnAllItemsDeletedListener {
         void onAllItemsDeleted (boolean yes);
    }

}
