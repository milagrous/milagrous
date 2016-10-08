package arm.milagrous.dbConnection;

import io.realm.RealmObject;

/**
 * Created by movses on 10/8/16.
 */
public class Record extends RealmObject {

    private String type;
    private int count;
    private long latitude;
    private long longitude;
    private long altitude;


    public void setType(String type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(long altitude) {
        this.altitude = altitude;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public long getAltitude() {
        return altitude;
    }

}
