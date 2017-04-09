package ims.com.employee.Models;

import ims.com.employee.prefs.DailyRec;

/**
 * Created by DravitLochan on 09-04-2017.
 */

public class DailyRecord {
    String checkInTime, checOutTime;
    Float expenses;
    public DailyRecord(String checkInTime, String checOutTime, Float expenses)
    {
        this.checkInTime = checkInTime;
        this.checOutTime = checOutTime;
        this.expenses = expenses;
    }
    public void setCheckInTime(String checkInTime)
    {
        this.checkInTime = checkInTime;
    }

    public void setChecOutTime(String checOutTime)
    {
        this.checOutTime = checOutTime;
    }

    public void setExpenses(Float expenses)
    {
        this.expenses = expenses;
    }

    public String getCheckInTime()
    {
        return checkInTime;
    }

    public String getChecOutTime()
    {
        return checOutTime;
    }
}
