package ims.com.employee.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ims.com.employee.Models.EmployeeCheckInModel;

/**
 * Created by therealshabi on 20/04/17.
 */

public class EmployeeDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Employee";

    private static final String CHECK_IN_TABLE_NAME = "Employee_check_in";
    private static final String CHECK_IN_TIME = "Employee_check_in_time";
    private static final String CHECK_IN_LOCATION = "Employee_check_in_location";
    private static final String CHECK_IN_LOCATION_DETAILS = "Employee_check_in_location_details";
    private static final String CHECK_IN_DESCRIPTION = "Employee_check_in_description";


    public EmployeeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String checkInTable = "CREATE TABLE " + CHECK_IN_TABLE_NAME + " (" +
                CHECK_IN_TIME + " VARCHAR, " +
                CHECK_IN_LOCATION + " VARCHAR, " +
                CHECK_IN_LOCATION_DETAILS + " VARCHAR, " +
                CHECK_IN_DESCRIPTION + " VARCHAR " +
                ");";

        db.execSQL(checkInTable);

    }

    public void insertNewCheckInDetails(EmployeeCheckInModel employee) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECK_IN_TIME, employee.getCheck_in_time());
        contentValues.put(CHECK_IN_LOCATION, employee.getLocation());
        contentValues.put(CHECK_IN_LOCATION_DETAILS, employee.getLoc_details());
        contentValues.put(CHECK_IN_DESCRIPTION, employee.getDescription());

        db.insert(CHECK_IN_TABLE_NAME, null, contentValues);
    }

    public EmployeeCheckInModel getCheckInDetails() {
        SQLiteDatabase db = getReadableDatabase();

        EmployeeCheckInModel employee = new EmployeeCheckInModel();

        String sql = "SELECT * FROM " + CHECK_IN_TABLE_NAME;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            employee.setCheck_in_time(cursor.getString(cursor.getColumnIndex(CHECK_IN_TIME)));
            employee.setLocation(cursor.getString(cursor.getColumnIndex(CHECK_IN_LOCATION)));
            employee.setLoc_details(cursor.getString(cursor.getColumnIndex(CHECK_IN_LOCATION_DETAILS)));
            employee.setDescription(cursor.getString(cursor.getColumnIndex(CHECK_IN_DESCRIPTION)));
        }

        cursor.close();

        return employee;
    }

    public void deleteCheckInDetails() {
        SQLiteDatabase db = getWritableDatabase();

        String deleteQuery = "DELETE FROM " + CHECK_IN_TABLE_NAME + ";";

        db.execSQL(deleteQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
