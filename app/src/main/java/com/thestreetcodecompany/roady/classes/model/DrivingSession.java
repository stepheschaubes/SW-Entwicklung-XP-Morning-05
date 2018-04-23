package com.thestreetcodecompany.roady.classes.model;

import com.orm.SugarRecord;
import com.thestreetcodecompany.roady.classes.DBHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.thestreetcodecompany.roady.classes.Helper.MakeToast;

/**
 * Created by Rutter on 23.03.2018.
 * Last changed by Schauberger on 23.04.2018
 */

public class DrivingSession extends SugarRecord {

    //id
    boolean active;
    String name;
    Date dateTime_start;
    Date dateTime_end;
    Car car;
    float km_start;
    float km_end;
    CoDriver coDriver;
    int weather;
    int street_condition;
    User user;
    boolean deleted;


    public DrivingSession() {}

    public DrivingSession(boolean active, String dateTime_start, float km_start, User user) {
        this.active = active;
        setDateTimeStringStart(dateTime_start);
        this.km_start = km_start;
        this.user = user;
    }

    public DrivingSession(String name, Date dateTime_start, Date dateTime_end, String car,
                          float km_start, float km_end, User user, String coDriver, int weather, int street_condition) {

        setName(name);

        setDateTimeStart(dateTime_start);
        setDateTimeEnd(dateTime_end);

        setCar(car);

        setKmStart(km_start);
        setKmEnd(km_end);

        setUser(user);
        setCoDriver(coDriver);

        setWeather(weather);
        setStreetCondition(street_condition);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateTimeStart(Date dateTime_start) {
        this.dateTime_start = dateTime_start;
    }

    public void setDateTimeEnd(Date dateTime_end) {
        this.dateTime_end = dateTime_end;
    }

    public void setDateTimeStringStart(String dateTime) {
        this.dateTime_start = formatDateTime(dateTime);
    }

    public void setDateTimeStringEnd(String dateTime) {
        this.dateTime_end = formatDateTime(dateTime);
    }

    public void setCar(String car) {
        List<Car> cars = Car.find(Car.class, "name = ?", car);
        if (cars.size() <= 0) {
            this.car = null;
        } else {
            this.car = cars.get(0);
        }
    }

    public void setKmStart(float km_start) {
        this.km_start = km_start;
    }

    public void setKmEnd(float km_end) {
        this.km_end = km_end;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCoDriver(String coDriver) {
        List<CoDriver> coDrivers = CoDriver.find(CoDriver.class, "name = ?", coDriver);
        if (coDrivers.size() <= 0) {
            // DB Connect
            DBHandler dbh = new DBHandler();
            this.coDriver = dbh.getTestCoDriver();
        } else {
            this.coDriver = coDrivers.get(0);
        }
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public void setStreetCondition(int street_condition) {
        this.street_condition = street_condition;
    }


    public int getWeather() {
        return this.weather;
    }


    public String getName()
    {
        if (this.name != null && !this.name.isEmpty()) {
            return this.name;
        } else {
            return "Graz - Wien (Dummy)";
        }
    }

    public float getDistance()
    {
        float distance = 0;
        if ((this.km_end - this.km_start) > 0) {
            distance = this.km_end - this.km_start;
        } else {
            distance = (float) 0.0;
        }
        return distance;
    }

    public String getTimeSpan()
    {
        //TODO: Datum ausgeben
        return "02.05.2018";
    }


    public Date formatDateTime(String dateTime) {
        String pattern = "dd-mm-yyyy hh:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date();
        //date.setHours(0);
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
