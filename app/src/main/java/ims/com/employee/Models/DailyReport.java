package ims.com.employee.Models;

import android.location.Location;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DravitLochan on 22-03-2017.
 */

public class DailyReport{

    @PrimaryKey
    private int id;
    private List<String> places;
    private  List<Location> locations;
    private float expences;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public List<String> getPlaces()
    {
        return places;
    }
    public void addPlace(String place)
    {
        places.add(place);
    }

    public void addLocation(Location location)
    {
        locations.add(location);
    }
}
