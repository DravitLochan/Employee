package ims.com.employee.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ims.com.employee.Models.EmployeeCheckInModel;

/**
 * Created by therealshabi on 20/04/17.
 */

public class EmployeeDatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Employee";
    private static final String TABLE_NAME = "EmployeeDetails";

    private static final String ID = "id";

    private static final String CHECK_IN_TIME = "check_in_time";
    private static final String CHECK_IN_LOCATION = "check_in_location";
    private static final String CHECK_IN_LOCATION_DETAILS = "check_in_details";
    private static final String CHECK_IN_DESCRIPTION = "check_in_description";
    private static final String CHECK_OUT_TIME = "checkout_time";
    private static final String CHECK_OUT_LOCATION = "check_in_location";
    private static final String CHECK_OUT_LOCATION_DETAILS = "check_out_details";
    private static final String CHECK_OUT_DESCRIPTION = "check_out_description";


    public EmployeeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String checkInTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY autoincrement, " +
                CHECK_IN_TIME + " TEXT, " +
                CHECK_IN_LOCATION + " TEXT, " +
                CHECK_IN_LOCATION_DETAILS + " TEXT, " +
                CHECK_IN_DESCRIPTION + " TEXT " +
                CHECK_OUT_TIME + " TEXT " +
                CHECK_OUT_LOCATION + " TEXT, " +
                CHECK_OUT_LOCATION_DETAILS + " TEXT, " +
                CHECK_OUT_DESCRIPTION + " TEXT " +
                ");";
        Log.i("table created", "true");
        db.execSQL(checkInTable);

    }

    public int insertCheckInDetails(EmployeeCheckInModel employee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECK_IN_TIME, employee.getIn_time());
        contentValues.put(CHECK_IN_LOCATION, employee.getIn_location());
        contentValues.put(CHECK_IN_LOCATION_DETAILS, employee.getIn_loc_details());
        contentValues.put(CHECK_IN_DESCRIPTION, employee.getIn_description());
        Log.i("message", "" + contentValues);
        db.insert(TABLE_NAME, null, contentValues);
        Cursor cursor = db.rawQuery("SELECT MAX(id) FROM "+ TABLE_NAME, null);
        return 1;
    }

    public void updateToCompleteEntry(int id, EmployeeCheckInModel employee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(CHECK_OUT_TIME, employee.getOut_time());
        contentValues.put(CHECK_OUT_LOCATION, employee.getOut_location());
        contentValues.put(CHECK_OUT_LOCATION_DETAILS, employee.getOut_loc_details());
        contentValues.put(CHECK_OUT_DESCRIPTION, employee.getOut_description());
        db.update(TABLE_NAME, contentValues, ID+" = ?", new String[]{id+""});
        Log.i("checkout dedtails added","successfully");
        db.insert(TABLE_NAME, null, contentValues);
    }

    public EmployeeCheckInModel getCheckInDetails(int id) {
        SQLiteDatabase db = getReadableDatabase();

        EmployeeCheckInModel employee = new EmployeeCheckInModel();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = "+ ID;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            employee.setIn_time(cursor.getString(cursor.getColumnIndex(CHECK_IN_TIME)));
            employee.setIn_location(cursor.getString(cursor.getColumnIndex(CHECK_IN_LOCATION)));
            employee.setIn_loc_details(cursor.getString(cursor.getColumnIndex(CHECK_IN_LOCATION_DETAILS)));
            employee.setIn_description(cursor.getString(cursor.getColumnIndex(CHECK_IN_DESCRIPTION)));
        }
        cursor.close();

        return employee;
    }

    public EmployeeCheckInModel getCheckOutDetails() {
        SQLiteDatabase db = getReadableDatabase();

        EmployeeCheckInModel employee = new EmployeeCheckInModel();

        String sql = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            employee.setOut_time(cursor.getString(cursor.getColumnIndex(CHECK_OUT_TIME)));
            employee.setOut_location(cursor.getString(cursor.getColumnIndex(CHECK_OUT_LOCATION)));
            employee.setOut_loc_details(cursor.getString(cursor.getColumnIndex(CHECK_OUT_LOCATION_DETAILS)));
            employee.setOut_description(cursor.getString(cursor.getColumnIndex(CHECK_OUT_DESCRIPTION)));
        }

        cursor.close();

        return employee;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlDelete = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sqlDelete);
    }
}
