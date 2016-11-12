package arm.milagrous.dbConnection;

import android.content.Context;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by movses on 10/8/16.
 */
public class RealmManager {

    Context context;
    private RealmManager () {

    }

    /**
     * @param context is the application context
     * @return a realManager instance
     */


    public static RealmManager getInstance(Context context) {
        RealmManager realmManager = new RealmManager ();
        realmManager.context = context;
        return realmManager;
    }

    /**
     * @param name is the table name
     * @return a <h2>Realm</h2> instance
     */
    public Realm createRealm(String name) {
        Realm.init(context);
        return Realm.getInstance(new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().name(name).build());
    }
}
