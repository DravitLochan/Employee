package ims.com.employee.Models;

import io.realm.RealmObject;

/**
 * Created by DravitLochan on 07-04-2017.
 */

public class LocationDets extends RealmObject {

    private String name, address, date, time;

    public LocationDets()
    {

    }

    public LocationDets(String name, String address, String date, String time) {
        this.name = name;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
