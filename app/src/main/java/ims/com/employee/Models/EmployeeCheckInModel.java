package ims.com.employee.Models;

/**
 * Created by therealshabi on 20/04/17.
 */

public class EmployeeCheckInModel {
    String in_time;
    String in_location;
    String in_loc_details;
    String in_description;
    String out_description;
    String out_time;
    String out_location;
    String out_loc_details;
    String reminder;


    public EmployeeCheckInModel(String in_time, String in_location, String in_loc_details, String in_description, String out_description, String out_time, String out_location, String out_loc_details, String reminder) {
        this.in_time = in_time;
        this.in_location = in_location;
        this.in_loc_details = in_loc_details;
        this.in_description = in_description;
        this.out_description = out_description;
        this.out_time = out_time;
        this.out_location = out_location;
        this.out_loc_details = out_loc_details;
        this.reminder = reminder;
    }

    public EmployeeCheckInModel() {

    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getIn_location() {
        return in_location;
    }

    public void setIn_location(String in_location) {
        this.in_location = in_location;
    }

    public String getIn_loc_details() {
        return in_loc_details;
    }

    public void setIn_loc_details(String in_loc_details) {
        this.in_loc_details = in_loc_details;
    }

    public String getIn_description() {
        return in_description;
    }

    public void setIn_description(String in_description) {
        this.in_description = in_description;
    }

    public String getOut_description() {
        return out_description;
    }

    public void setOut_description(String out_description) {
        this.out_description = out_description;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getOut_location() {
        return out_location;
    }

    public void setOut_location(String out_location) {
        this.out_location = out_location;
    }

    public String getOut_loc_details() {
        return out_loc_details;
    }

    public void setOut_loc_details(String out_loc_details) {
        this.out_loc_details = out_loc_details;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }
}
