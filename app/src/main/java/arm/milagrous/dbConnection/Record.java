package arm.milagrous.dbConnection;

import io.realm.RealmObject;

/**
 * Created by movses on 10/8/16.
 */
public class Record extends RealmObject {

    private String type;
    private int count;
    private double latitude;
    private double longitude;
    private double altitude;


    public void setType(String type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

}
