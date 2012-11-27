package jlamson.csci498.lunchlist;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

public class WidgetService extends IntentService {

	public WidgetService() {
		super("WidgetService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		
		ComponentName me = new ComponentName(this, AppWidget.class);
		RemoteViews updateViews = new RemoteViews("jlamson.csci498.lunchlist",
				R.layout.widget);
		RestaurantHelper helper = new RestaurantHelper(this);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		
		try {
			int count = helper.getCountRestaurants();
			
			if (count > 0) {
			
				Cursor c = helper.getRandomRestautant(count);
				
				updateViews.setTextViewText(R.id.name, c.getString(1));
				
				Intent i = new Intent(this, DetailForm.class);
				i.putExtra(LunchList.ID_EXTRA, c.getString(0));
				
				PendingIntent pi = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
				updateViews.setOnClickPendingIntent(R.id.name, pi);
			
			} else {
				updateViews.setTextViewText(R.id.name, this.getString(R.string.empty));
			}
			
		} finally {
			helper.close();
		}
		
		Intent i = new Intent(this, WidgetService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		
		updateViews.setOnClickPendingIntent(R.id.next, pi);
		manager.updateAppWidget(me, updateViews);
	}

}
