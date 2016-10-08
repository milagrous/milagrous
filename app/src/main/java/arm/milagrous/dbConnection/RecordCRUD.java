package arm.milagrous.dbConnection;

import android.content.Context;
import android.text.TextUtils;

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

    private RecordCRUD () {

    }

    public boolean CreateRealm (String name) {
        if (!(name == null)) {
            realm = RealmManager.getInstance (context).createRealm (name);
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
        if (isExist (getAllRecords(), record)) {
            createdCallback.RecordCreated(false);

            return false;
        }

        realm.beginTransaction ();
        Record newRecord = realm.createObject (Record.class);
        newRecord.setType(record.getType());
        newRecord.setCount(record.getCount());

        newRecord.setLatitude(record.getLatitude());
        newRecord.setLongitude(record.getLongitude());
        newRecord.setAltitude(record.getAltitude());

        realm.commitTransaction ();
        createdCallback.RecordCreated (true);

        return true;
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
     * @param Record     is the Record to be deleted and the
     * @param callback is for checking for success or failure of operation
     * @return <h3><i>true</i></h3> if Record is successfully deleted and <h3><i>false</i></h3> if otherwise
     */
    public boolean deleteRecord (Record Record, RecordDeletedCallback callback) {
        if (isExist (getAllRecords (), Record)) {
            realm.beginTransaction ();
            RealmResults<Record> realmResults = realm.where (Record.class).equalTo ("type", Record.getType()).findAll ();
            if (!realmResults.isEmpty ()) {
                for (int i = 0; i < realmResults.size (); i++) {
                    realmResults.get (i).deleteFromRealm ();
                }
            }
            realm.commitTransaction ();
            callback.RecordIsDeleted (true);

            return true;
        }
        callback.RecordIsDeleted (false);

        return false;
    }

    /**
     * @param record                 is the current Record to be updated
     * @param type                   is the name parameter to be used in updating the current Record
     * @param count                  is the address to be used in updating the current Record
     * @param updateCompleteListener is a listener callback for checking if the update is successful
     * @return boolean statement of <h3><i>true</i></h3> if update is successful and <h3><i>false</i></h3> if otherwise
     */
    public boolean updateRecord (Record record, String type, Integer count, Long longitute,
                                 Long lattitude, Long altitude, OnUpdateCompleteListener updateCompleteListener) {
        if (isExist (getAllRecords (), record)) {
            Record RecordToBeEdited = realm.where (Record.class)
                    .equalTo ("type", record.getType()).findFirst ();
            realm.beginTransaction ();
            RecordToBeEdited.setType(type);
// TODO update for record model
//            if (!count.isEmpty ())
//                RecordToBeEdited.setCount(count);
//            if (!longitute)
//                RecordToBeEdited.setCount (count);
//            if (!count.isEmpty ())
//                RecordToBeEdited.setCount (count);
//            if (!count.isEmpty ())
//                RecordToBeEdited.setCount (count);
//

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
     * @param records is the list of Records in the database
     * @param record  is the Record to confirm its existence in the database
     * @return <h3><i>true</i></h3> if Record found and <h3><i>false</i></h3> if otherwise
     */
    private boolean isExist (List<Record> records, Record record) {
        for (int i = 0; i < records.size (); i++) {
            Record record1 = records.get (i);
            if (record1.getType ().equals (record.getType ()))

                return true;
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
        public void RecordCreated (boolean yesNo);
    }

    public interface RecordDeletedCallback {
        public void RecordIsDeleted (boolean yesNo);
    }

    public interface OnUpdateCompleteListener {
        public void onUpdate (boolean yes);
    }

    public interface RecordFoundCompleteListener {
        public void onRecordFound (Record Record);
    }

    public interface OnAllItemsDeletedListener {
        public void onAllItemsDeleted (boolean yes);
    }

}
