package csci498.jlamson.lunchlist;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LunchList extends ListActivity {

	public final static String ID_EXTRA = "csci498.jlamson.lunchlist._ID";

	Cursor modelCursor;
	RestaurantAdapter restaurantAdapter = null;

	RestaurantHelper helper;

	private SharedPreferences preference;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initDatabaseAccess();
		initPreference();
		initRestaurantListView();
		
	}

	private void initDatabaseAccess() {
		helper = new RestaurantHelper(this);
	}

	private void initPreference() {
		preference = PreferenceManager.getDefaultSharedPreferences(this);
		preference.registerOnSharedPreferenceChangeListener(prefListener);
	}
	
	private void initRestaurantListView() {
		if (modelCursor != null) {
			stopManagingCursor(modelCursor);
			modelCursor.close();
		}
		
		modelCursor = helper.getAll(preference.getString("sort_order", "name"));
		startManagingCursor(modelCursor);
		restaurantAdapter = new RestaurantAdapter(modelCursor);
		setListAdapter(restaurantAdapter);
	}

	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			startActivity(new Intent(LunchList.this, DetailForm.class));
			return true;
		} else if (item.getItemId() == R.id.prefs) {
			startActivity(new Intent(LunchList.this, EditPreferences.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class RestaurantAdapter extends CursorAdapter {
		public RestaurantAdapter(Cursor c) {
			super(LunchList.this, c);
		}

		@Override
		public void bindView(View row, Context context, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();
			holder.populateFrom(c, helper);
		}

		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			LayoutInflater inflator = getLayoutInflater();
			View row = inflator.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);

			return row;
		}
	}

	static class RestaurantHolder {
		private TextView name = null;
		private TextView address = null;
		private ImageView icon = null;

		public RestaurantHolder(View row) {
			name = (TextView) row.findViewById(R.id.title);
			address = (TextView) row.findViewById(R.id.address);
			icon = (ImageView) row.findViewById(R.id.icon);
		}

		public void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));

			if (helper.getType(c).equals("sit_down")) {
				icon.setImageResource(R.drawable.ball_red);
			} else if (helper.getType(c).equals("take_out")) {
				icon.setImageResource(R.drawable.ball_yellow);
			} else {
				icon.setImageResource(R.drawable.ball_green);
			}
		}
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		Intent i = new Intent(LunchList.this, DetailForm.class);
		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener = 
		new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
				if (key.equals("sort_order")) {
					initRestaurantListView();
				}
			}
		};
}
