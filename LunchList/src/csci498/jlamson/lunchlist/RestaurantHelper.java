package csci498.jlamson.lunchlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.*;

public class RestaurantHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "lunchlist.db";
	private static final int SCHEMA_VERSION = 1;

	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurants (" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"name TEXT, " +
				"address TEXT, " +
				"type TEXT" +
				"notes TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//no action until version 2+ is implemented
	}
	
	public void insert(Restaurant r) {
		
		ContentValues cv = new ContentValues();
		
		cv.put("name", r.getName());
		cv.put("address", r.getAddress());
		cv.put("type", r.getType());
		cv.put("notes", r.getNotes());
		
		getWritableDatabase().insert("restaurants", "name", cv);
		
	}

}
