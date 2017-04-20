package ims.com.employee.Models;

/**
 * Created by therealshabi on 20/04/17.
 */

public class EmployeeCheckInModel {
    String check_in_time;
    String location;
    String loc_details;
    String description;
    String check_out_description;
    String check_out_time;
    String reminder;

    public String getCheck_out_description() {
        return check_out_description;
    }

    public void setCheck_out_description(String check_out_description) {
        this.check_out_description = check_out_description;
    }

    public String getCheck_out_time() {
        return check_out_time;
    }

    public void setCheck_out_time(String check_out_time) {
        this.check_out_time = check_out_time;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getCheck_in_time() {
        return check_in_time;
    }

    public void setCheck_in_time(String check_in_time) {
        this.check_in_time = check_in_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLoc_details() {
        return loc_details;
    }

    public void setLoc_details(String loc_details) {
        this.loc_details = loc_details;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
