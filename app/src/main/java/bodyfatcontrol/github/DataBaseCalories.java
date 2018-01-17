package bodyfatcontrol.github;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseCalories extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cals.db";
    private static final String TABLE_NAME = "cals_out";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_HR = "hr";
    private static final String COLUMN_CALORIES_PER_MINUTE_VALUE = "cals_minute";
    private static final String COLUMN_CALORIES_EER_PER_MINUTE_VALUE = "cals_eer_minute";

    public DataBaseCalories () {
        super(MainActivity.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " integer UNIQUE, " + /* UNIQUE means that there will not be duplicate entries with the same date */
                COLUMN_HR + " integer," +
                COLUMN_CALORIES_PER_MINUTE_VALUE + " real," +
                COLUMN_CALORIES_EER_PER_MINUTE_VALUE + " real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void DataBaseWriteMeasurement (ArrayList<Measurement> measurementList) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        for (Measurement measurement : measurementList) {
            values.put(COLUMN_DATE, measurement.getDate());
            values.put(COLUMN_HR, measurement.getHR());
            values.put(COLUMN_CALORIES_PER_MINUTE_VALUE, measurement.getCaloriesPerMinute());
            values.put(COLUMN_CALORIES_EER_PER_MINUTE_VALUE, measurement.getCaloriesEERPerMinute());

            // Inserting Row
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

        db.close(); // Closing database connection
    }

    public void DataBaseWriteMeasurement (Measurement measurement) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_DATE, measurement.getDate());
        values.put(COLUMN_HR, measurement.getHR());
        values.put(COLUMN_CALORIES_PER_MINUTE_VALUE, measurement.getCaloriesPerMinute());
        values.put(COLUMN_CALORIES_EER_PER_MINUTE_VALUE, measurement.getCaloriesEERPerMinute());

        // Inserting Row
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close(); // Closing database connection
    }

    public ArrayList<Measurement> DataBaseGetMeasurements (long initialDate, long finalDate) {
        // Query to get all the records starting at last midnight, ordered by date ascending
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " BETWEEN " +
                + initialDate + " AND " + finalDate + " ORDER BY " + COLUMN_DATE +
                " ASC";

        Cursor cursor = db.rawQuery(query, null);

        // Loop to put all the values to the ArrayList<Measurement>
        cursor.moveToFirst();
        int counter = cursor.getCount();
        int date;
        int HR;
        int caloriesPerMinute;
        int caloriesEERPerMinute;
        ArrayList<Measurement> measurementList = new ArrayList<Measurement>();
        for ( ; counter > 0; ) {
            if (cursor.isAfterLast()) break;
            date = cursor.getInt(cursor.getColumnIndex(COLUMN_DATE));
            HR = cursor.getInt(cursor.getColumnIndex(COLUMN_HR));
            caloriesPerMinute = cursor.getInt(cursor.getColumnIndex(COLUMN_CALORIES_PER_MINUTE_VALUE));
            caloriesEERPerMinute = cursor.getInt(cursor.getColumnIndex(COLUMN_CALORIES_EER_PER_MINUTE_VALUE));
            cursor.moveToNext();

            Measurement measurement = new Measurement();
            measurement.setDate(date);
            measurement.setHR(HR);
            measurement.setCaloriesPerMinute(caloriesPerMinute);
            measurement.setCaloriesEERPerMinute(caloriesEERPerMinute);
            measurementList.add(measurement);
        }

        cursor.close();
        db.close(); // Closing database connection
        return measurementList;
    }

    public long DataBaseGetLastMeasurementDate() {
        SQLiteDatabase db = this.getWritableDatabase();
        // build the query
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " DESC LIMIT 1";
        // open database
        Cursor cursor = db.rawQuery(query, null);
        long date = 0;
        if (cursor.moveToFirst() == true) { // if cursor is not empty
            date = cursor.getInt(cursor.getColumnIndex(COLUMN_DATE));
        }

        cursor.close();
        db.close(); // Closing database connection
        return date;
    }
}
