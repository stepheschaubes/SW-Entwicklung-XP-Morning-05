package com.thestreetcodecompany.roady.classes.model;

import android.media.MediaCas;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Rutter on 23.03.2018.
 * Last changed by Schauberger on 24.04.2018
 */

public class User extends SugarRecord {

    //int id;
    private String name;
    // TODO: in the Settings we can change the driven_kms (before the start of using this app)..
    // So everything is wrong then
    // maybe make getter to sum up original driven kilometers + all the distances from the Driving Session
    private float driven_km;
    private float goal_km;

    public boolean getPushes() {
        return pushes;
    }

    public void setPushes(boolean pushes) {
        this.pushes = pushes;
    }

    boolean pushes;
    boolean deleted;

    public User(){
        pushes = false;
        goal_km = 1;
        driven_km = 0;
    }
    public User(String name, float driven_km, float goal_km) {
        setName(name);
        setDrivenKm(driven_km);
        setGoalKm(goal_km);
    }



    // toString
    public String toString() {
        return "name: " + getName() + ", driven_km: " + getDrivenKm() + ", goal_km: " + getGoalKm();
    }


    // getter
    public String getName() {
        return this.name;
    }

    public float getDrivenKm() {
        return driven_km;
    }

    public float getGoalKm() {
        return this.goal_km;
    }

    public void addDriven_km(float distance)
    {
        driven_km += distance;
    }

    // setter
    public void setName(String name) {
        this.name = name;
    }

    public void setDrivenKm(float driven_km) {
        this.driven_km = driven_km;
    }

    public void setGoalKm(float goal_km) {
        this.goal_km = goal_km;
    }

    // additional
    public List<Car> getCars() {
        return Car.find(Car.class, "user = ?", "" + getId());
    }

    public List<CoDriver> getCoDrivers() {
        return CoDriver.find(CoDriver.class, "user = ?", "" + getId());
    }

    public List<Achievement> getAchievements() {
        return Achievement.listAll(Achievement.class);
    }

    public List<Achievement> getAchievementsCondition() {
        return Achievement.find(Achievement.class, "type <= ?", "3");
    }

    public List<Achievement> getAchievementsType(int type) {
        return Achievement.find(Achievement.class, "type = ?", "" + type);
    }

    public List<Achievement> getAchievementsTypeReached(int type) {
        List<Achievement> achievementsAll = getAchievementsType(type);
        final List<Achievement> achievements = Achievement.find(Achievement.class, "type < ?", "0");
        for (Achievement achievement : achievementsAll) {
            if (achievement.getReached()) {
                achievements.add(achievement);
            }
        }
        return achievements;
    }

    public Achievement getLatestAchievement() {
        List<Achievement> achievements = getAchievementsTypeReached(7);
        if (!achievements.isEmpty()) {
            return achievements.get(achievements.size() - 1);
        } else {
            return null;
        }
    }

    public List<Achievement> getUserGeneratedAchievements() {
        return Achievement.find(Achievement.class, "type = ?", "10" );
    }

    public DrivingSession getLastDrivingSession()
    {
        List<DrivingSession> list = DrivingSession.listAll(DrivingSession.class);
        if(list.size() > 0)
        {
            return list.get(list.size() - 1);
        }

        return null;
    }





    public long getLastDrivingSessionID()
    {
        List<DrivingSession> list = DrivingSession.find(DrivingSession.class,"user = ?", "" + getId());
        if(list.size() > 0)
        {
            DrivingSession last_ds = list.get(list.size() - 1);
            return last_ds.getId();
        }

        return -1;
    }





    public long getTimeSinceLastDrivingSession()
    {
        DrivingSession last_ds = getLastDrivingSession();
        if(last_ds != null)
        {
            Log.d("USER", "name: " + last_ds.getName());
            long currentTime = Calendar.getInstance().getTimeInMillis();
            return currentTime - last_ds.getDateTimeEnd();
        }

        return 0;

    }

    public boolean updatelastDrivingSession()
    {
        DrivingSession last_ds = getLastDrivingSession();
        if(last_ds != null)
        {
         last_ds.executeQuery("DELETE from last_ds where name = 'undefined'");
        }

        return true;
    }




    public DrivingSession hasActiveDrivingSession()
    {
        Log.d("User","hasActiveDrivingSession()");
        DrivingSession last_ds = getLastDrivingSession();
        if(last_ds != null && (last_ds.getActive()==true))
        {
            return last_ds;
        }
        return null;
    }


    public List<DrivingSession> getAllDrivingSessions()
    {
        return DrivingSession.findWithQuery(DrivingSession.class, "SELECT * FROM driving_session WHERE user = ? ORDER BY date_timestart DESC", "" + getId());
    }

    public List<FileHistory> getAllFileHistories()
    {
        return FileHistory.find(FileHistory.class,"user = ?", "" + getId());
    }
}
